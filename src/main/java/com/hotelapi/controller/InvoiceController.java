package com.hotelapi.controller;

import com.hotelapi.service.InvoiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;

@RestController
@RequestMapping("/api/invoice")
@Tag(name = "Invoice", description = "API for PDF Invoice Generation")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @GetMapping("/download/{billId}")
    @Operation(summary = "Download invoice PDF by Bill ID")
    public ResponseEntity<?> downloadInvoice(@PathVariable Long billId) {
        try {
            String filePath = invoiceService.generateInvoicePdf(billId);
            File file = new File(filePath);

            if (!file.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Invoice file not found.");
            }

            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentDisposition(ContentDisposition.builder("attachment")
                    .filename(file.getName()).build());
            headers.setContentType(MediaType.APPLICATION_PDF);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error generating invoice: " + e.getMessage());
        }
    }
}
