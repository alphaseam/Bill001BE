package com.hotelapi.service;

import com.hotelapi.dto.WhatsAppMessageDto;
import com.hotelapi.entity.Bill;
import com.hotelapi.entity.Customer;
import com.hotelapi.repository.BillRepository;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Schema(description = "Service layer for handling Bill creation and post-processing")
public class BillService {

    private final BillRepository billRepository;
    private final InvoiceService invoiceService;
    private final WhatsAppService whatsAppService;

    /**
     * Creates a bill and sends WhatsApp notification.
     * Handles PDF generation exceptions gracefully to avoid blocking bill save.
     *
     * @param bill Bill entity to be saved
     * @return saved Bill
     */
    @Transactional
    public Bill createBillWithNotification(Bill bill) {
        // Save the bill to database
        Bill savedBill = billRepository.save(bill);

        Customer customer = savedBill.getCustomer();

        // Attempt to generate PDF and prepare the WhatsApp link
        String pdfUrl = null;
        try {
            pdfUrl = invoiceService.generateInvoicePdf(savedBill.getId());
            pdfUrl = "https://yourdomain.com" + pdfUrl; //  deployed domain
        } catch (Exception e) {
            log.error("Failed to generate invoice PDF for bill ID {}: {}", savedBill.getId(), e.getMessage(), e);//logger to test
        }

        // Build WhatsApp message DTO
        WhatsAppMessageDto dto = WhatsAppMessageDto.builder()
                .billId(savedBill.getId())
                .customerName(customer.getName())
                .customerPhone(customer.getMobile())  
                .billPdfUrl(pdfUrl)                   //  null if generation failed
                .messageContent("Your bill total is ₹" + savedBill.getTotal() + ".")
                .build();

        // Sending WhatsApp message 
        whatsAppService.sendMessage(dto);

        return savedBill;
    }
}
