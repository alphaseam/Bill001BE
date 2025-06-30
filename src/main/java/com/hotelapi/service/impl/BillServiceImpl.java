package com.hotelapi.service.impl;

import com.hotelapi.dto.BillDto;
import com.hotelapi.dto.BillItemDto;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * JIRA-101: Create Bill (POST)
     */
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

        double tax = subtotal * 0.12; // 12% tax example
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

        // link bill items back to this bill
        billItems.forEach(item -> item.setBill(bill));

        return createBillWithNotification(bill);
    }

    /**
     * Save bill and optionally notify via WhatsApp
     */
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

    /**
     * JIRA-102: Get Bill by ID (GET)
     */
    @Override
    public Bill getBillById(Long id) {
        return billRepository.findById(id)
                .orElseThrow(() -> new BillNotFoundException("Bill not found with ID: " + id));
    }

    /**
     * JIRA-103: Get All Bills (GET)
     */
    @Override
    public List<Bill> getAllBills() {
        List<Bill> bills = billRepository.findAll();
        if (bills.isEmpty()) {
            log.info("No bills found in database.");
        }
        return bills;
    }

    /**
     * JIRA-104: Update Bill (PATCH)
     */
    @Override
    @Transactional
    public Bill updateBill(Long id, BillDto dto) {
        Bill existingBill = billRepository.findById(id)
                .orElseThrow(() -> new BillNotFoundException("Bill not found with ID: " + id));

        // update only if present in DTO
        if (dto.getDiscount() != null) existingBill.setDiscount(dto.getDiscount());
        if (dto.getSubtotal() != null) existingBill.setSubtotal(dto.getSubtotal());
        if (dto.getTax() != null) existingBill.setTax(dto.getTax());
        if (dto.getTotal() != null) existingBill.setTotal(dto.getTotal());

        // PATCH does not update items or customer
        return billRepository.save(existingBill);
    }

    /**
     * JIRA-105: Delete Bill (DELETE)
     */
    @Override
    @Transactional
    public void deleteBill(Long id) {
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new BillNotFoundException("Cannot delete. Bill not found with ID: " + id));

        billRepository.delete(bill);
    }
}
