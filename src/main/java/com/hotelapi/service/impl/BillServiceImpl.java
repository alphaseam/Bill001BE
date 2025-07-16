package com.hotelapi.service.impl;

import com.hotelapi.dto.BillDto;
import com.hotelapi.dto.BillItemDto;
import com.hotelapi.dto.BillStatsDto;
import com.hotelapi.dto.MobileBillRequest;
import com.hotelapi.dto.MobileBillResponse;
import com.hotelapi.dto.WhatsAppMessageRequest;
import com.hotelapi.entity.*;
import com.hotelapi.exception.*;
import com.hotelapi.repository.*;
import com.hotelapi.service.BillService;
import com.hotelapi.service.InvoiceService;
import com.hotelapi.service.WhatsAppService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BillServiceImpl implements BillService {

    private final BillRepository billRepository;
    private final BillItemRepository billItemRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final InvoiceService invoiceService;
    private final WhatsAppService whatsAppService;

    @Override
    @Transactional
    public Bill createBill(BillDto dto) {
        if (dto.getCustomerId() == null || dto.getItems() == null || dto.getItems().isEmpty()) {
            throw new InvalidInputException("Customer ID and at least one bill item are required.");
        }

        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new InvalidInputException("Customer not found: ID " + dto.getCustomerId()));

        List<BillItem> billItems = new ArrayList<>();
        double subtotal = 0;

        for (BillItemDto itemDto : dto.getItems()) {
            Product product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new InvalidInputException("Product not found: ID " + itemDto.getProductId()));

            if (itemDto.getQuantity() <= 0) {
                throw new InvalidInputException("Quantity must be positive for product ID " + itemDto.getProductId());
            }

            double itemTotal = itemDto.getQuantity() * itemDto.getPrice();
            subtotal += itemTotal;

            billItems.add(BillItem.builder()
                    .itemName(product.getProductName())
                    .quantity(itemDto.getQuantity())
                    .unitPrice(itemDto.getPrice())
                    .discount(0.0)
                    .total(itemTotal)
                    .build());
        }

        double tax = subtotal * 0.12;
        double discount = dto.getDiscount() != null ? dto.getDiscount() : 0.0;
        double total = subtotal + tax - discount;

        Bill bill = Bill.builder()
                .billNumber("INV-" + System.currentTimeMillis())
                .createdAt(LocalDateTime.now())
                .customer(customer)
                .items(billItems)
                .subtotal(subtotal)
                .tax(tax)
                .discount(discount)
                .total(total)
                .build();

        billItems.forEach(item -> item.setBill(bill));

        return createBillWithNotification(bill);
    }

    public Bill createBillWithNotification(Bill bill) {
        Bill savedBill = billRepository.save(bill);

        Customer customer = savedBill.getCustomer();
        String pdfUrl = null;

        try {
            pdfUrl = invoiceService.generateInvoicePdf(savedBill.getId());
            pdfUrl = "https://yourdomain.com" + pdfUrl;
        } catch (Exception e) {
            log.error("Failed to generate invoice PDF for bill ID {}: {}", savedBill.getId(), e.getMessage(), e);
        }

        WhatsAppMessageRequest dto = WhatsAppMessageRequest.builder()
                .billId(savedBill.getId())
                .customerName(customer.getName())
                .customerPhone(customer.getMobile())
                .billPdfUrl(pdfUrl)
                .messageContent("Your bill total is ₹" + savedBill.getTotal() + ".")
                .build();

        whatsAppService.sendMessage(dto);

        return savedBill;
    }

    @Override
    public Bill getBillById(Long id) {
        return billRepository.findById(id)
                .orElseThrow(() -> new BillNotFoundException("Bill not found with ID: " + id));
    }

    @Override
    public List<Bill> getAllBills() {
        List<Bill> bills = billRepository.findAll();
        if (bills.isEmpty()) {
            log.info("No bills found in database.");
        }
        return bills;
    }

    @Override
    @Transactional
    public Bill updateBill(Long id, BillDto dto) {
        Bill existingBill = billRepository.findById(id)
                .orElseThrow(() -> new BillNotFoundException("Bill not found with ID: " + id));

        try {
            boolean updated = false;

            // Update bill items if provided
            if (dto.getItems() != null && !dto.getItems().isEmpty()) {
                // Get existing items
                List<BillItem> existingItems = existingBill.getItems();
                
                // Update existing items based on productId matching
                for (BillItemDto itemDto : dto.getItems()) {
                    // Find the existing item by matching productId or by position
                    BillItem existingItem = existingItems.stream()
                            .filter(item -> {
                                // Try to find product by name or by position
                                Product product = productRepository.findById(itemDto.getProductId()).orElse(null);
                                return product != null && item.getItemName().equals(product.getProductName());
                            })
                            .findFirst()
                            .orElse(null);
                    
                    if (existingItem != null) {
                        // Update quantity and price
                        if (itemDto.getQuantity() != null) {
                            existingItem.setQuantity(itemDto.getQuantity());
                        }
                        if (itemDto.getPrice() != null) {
                            existingItem.setUnitPrice(itemDto.getPrice());
                        }
                        
                        // Recalculate item total
                        double itemTotal = existingItem.getQuantity() * existingItem.getUnitPrice();
                        existingItem.setTotal(itemTotal);
                        updated = true;
                    }
                }
                
                // Recalculate subtotal, tax, and total after item updates
                if (updated) {
                    double newSubtotal = existingItems.stream().mapToDouble(BillItem::getTotal).sum();
                    existingBill.setSubtotal(newSubtotal);
                    
                    // Apply discount if provided, otherwise keep existing
                    double discount = dto.getDiscount() != null ? dto.getDiscount() : existingBill.getDiscount();
                    existingBill.setDiscount(discount);
                    
                    // Calculate tax (12%)
                    double tax = newSubtotal * 0.12;
                    existingBill.setTax(tax);
                    
                    // Calculate final total
                    existingBill.setTotal(newSubtotal + tax - discount);
                }
            }

            // Update other bill-level fields if items were not updated
            if (!updated) {
                if (dto.getDiscount() != null) {
                    existingBill.setDiscount(dto.getDiscount());
                    // Recalculate total with new discount
                    existingBill.setTotal(existingBill.getSubtotal() + existingBill.getTax() - dto.getDiscount());
                    updated = true;
                }
                if (dto.getSubtotal() != null) {
                    existingBill.setSubtotal(dto.getSubtotal());
                    updated = true;
                }
                if (dto.getTax() != null) {
                    existingBill.setTax(dto.getTax());
                    updated = true;
                }
                if (dto.getTotal() != null) {
                    existingBill.setTotal(dto.getTotal());
                    updated = true;
                }
            }

            if (!updated) {
                throw new InvalidUpdateException("No valid fields provided for update.");
            }

            return billRepository.save(existingBill);

        } catch (InvalidUpdateException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while updating bill ID {}: {}", id, e.getMessage(), e);
            throw new DatabaseException("Internal error occurred while updating the bill.");
        }
    }

    @Override
    @Transactional
    public void deleteBill(Long id) {
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new BillNotFoundException("Cannot delete. Bill not found with ID: " + id));
        billRepository.delete(bill);
    }

    @Override
    @Transactional
    public MobileBillResponse createMobileBill(MobileBillRequest request) {
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new InvalidInputException("At least one item is required to create a bill.");
        }

        if (request.getCustomerName() == null || request.getCustomerName().isBlank()) {
            throw new InvalidInputException("Customer name is required.");
        }

        if (request.getMobileNumber() == null || request.getMobileNumber().isBlank()) {
            throw new InvalidInputException("Customer mobile number is required.");
        }

        Customer customer = customerRepository.findByMobile(request.getMobileNumber())
                .orElseGet(() -> Customer.builder()
                        .name(request.getCustomerName())
                        .mobile(request.getMobileNumber())
                        .build());

        List<BillItem> billItems = new ArrayList<>();
        double subtotal = 0;

        for (MobileBillRequest.Item item : request.getItems()) {
            if (item.getQuantity() <= 0) {
                throw new InvalidInputException("Quantity must be positive for product: " + item.getProductName());
            }

            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new InvalidInputException("Product not found with ID: " + item.getProductId()));

            double unitPrice = item.getUnitPrice();
            double discount = item.getDiscount() != null ? item.getDiscount() : 0.0;
            double itemTotal = (item.getQuantity() * unitPrice) - discount;
            subtotal += itemTotal;

            billItems.add(BillItem.builder()
                    .itemName(product.getProductName())
                    .quantity(item.getQuantity())
                    .unitPrice(unitPrice)
                    .discount(discount)
                    .total(itemTotal)
                    .build());
        }

        double tax = subtotal * 0.12;
        double total = subtotal + tax;

        Bill bill = Bill.builder()
                .billNumber("MBL-" + System.currentTimeMillis())
                .createdAt(LocalDateTime.now())
                .customer(customer)
                .items(billItems)
                .subtotal(subtotal)
                .tax(tax)
                .discount(0.0)
                .total(total)
                .build();

        billItems.forEach(item -> item.setBill(bill));
        Bill savedBill = billRepository.save(bill);

        if (customer.getId() == null) {
            customerRepository.save(customer);
        }

        List<MobileBillResponse.ItemSummary> itemSummaries = new ArrayList<>();
        for (BillItem bi : billItems) {
            MobileBillResponse.ItemSummary summary = new MobileBillResponse.ItemSummary();
            summary.setProductName(bi.getItemName());
            summary.setQuantity(bi.getQuantity());
            summary.setTotal(bi.getTotal());
            itemSummaries.add(summary);
        }

        MobileBillResponse response = new MobileBillResponse();
        response.setMessage("Bill created successfully");
        response.setBillId(savedBill.getId());
        response.setTotalAmount(savedBill.getTotal());
        response.setBillDate(LocalDate.now());
        response.setItems(itemSummaries);

        return response;
    }

    @Override
    public List<Bill> getBillsByUserId(Long userId) {
        return billRepository.findByUserId(userId);
    }

    @Override
    public List<Bill> getBillsByProductName(String productName) {
        return billRepository.findByItems_ItemNameContainingIgnoreCase(productName);
    }

    @Override
    public List<Bill> getBillsByDateRange(Long userId, String from, String to) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime fromDate = LocalDate.parse(from, formatter).atStartOfDay();
        LocalDateTime toDate = LocalDate.parse(to, formatter).atTime(23, 59, 59);
        
        return billRepository.findByCreatedAtBetweenAndUserId(fromDate, toDate, userId);
    }

    @Override
    public BillStatsDto getBillStats(String type) {
        List<Bill> allBills = billRepository.findAll();
        
        BillStatsDto stats = new BillStatsDto();
        stats.setType(type);
        stats.setTotalBills((long) allBills.size());
        stats.setTotalRevenue(allBills.stream().mapToDouble(Bill::getTotal).sum());
        stats.setAverageBillAmount(allBills.stream().mapToDouble(Bill::getTotal).average().orElse(0.0));
        
        List<BillStatsDto.StatItem> details = new ArrayList<>();
        
        if ("monthly".equals(type)) {
            Map<String, List<Bill>> monthlyBills = allBills.stream()
                    .collect(Collectors.groupingBy(bill -> 
                        bill.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM"))));
            
            for (Map.Entry<String, List<Bill>> entry : monthlyBills.entrySet()) {
                BillStatsDto.StatItem item = new BillStatsDto.StatItem();
                item.setLabel(entry.getKey());
                item.setCount((long) entry.getValue().size());
                item.setAmount(entry.getValue().stream().mapToDouble(Bill::getTotal).sum());
                item.setPeriod(entry.getKey());
                details.add(item);
            }
        } else if ("weekly".equals(type)) {
            Map<String, List<Bill>> weeklyBills = allBills.stream()
                    .collect(Collectors.groupingBy(bill -> 
                        bill.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-ww"))));
            
            for (Map.Entry<String, List<Bill>> entry : weeklyBills.entrySet()) {
                BillStatsDto.StatItem item = new BillStatsDto.StatItem();
                item.setLabel("Week " + entry.getKey());
                item.setCount((long) entry.getValue().size());
                item.setAmount(entry.getValue().stream().mapToDouble(Bill::getTotal).sum());
                item.setPeriod(entry.getKey());
                details.add(item);
            }
        } else if ("productwise".equals(type)) {
            Map<String, List<BillItem>> productItems = new HashMap<>();
            for (Bill bill : allBills) {
                for (BillItem item : bill.getItems()) {
                    productItems.computeIfAbsent(item.getItemName(), k -> new ArrayList<>()).add(item);
                }
            }
            
            for (Map.Entry<String, List<BillItem>> entry : productItems.entrySet()) {
                BillStatsDto.StatItem item = new BillStatsDto.StatItem();
                item.setLabel(entry.getKey());
                item.setCount((long) entry.getValue().size());
                item.setAmount(entry.getValue().stream().mapToDouble(BillItem::getTotal).sum());
                details.add(item);
            }
        }
        
        stats.setDetails(details);
        return stats;
    }
}
