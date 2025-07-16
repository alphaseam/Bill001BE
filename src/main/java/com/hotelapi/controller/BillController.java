package com.hotelapi.controller;

import com.hotelapi.dto.BillDto;
import com.hotelapi.dto.BillStatsDto;
import com.hotelapi.dto.MobileBillRequest;
import com.hotelapi.dto.MobileBillResponse;
import com.hotelapi.entity.Bill;
import com.hotelapi.service.BillService;
import com.hotelapi.service.InvoiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/bill")
@Tag(name = "Bill", description = "Bill creation and management")
@RequiredArgsConstructor
public class BillController {

    private final BillService billService;
    private final InvoiceService invoiceService;

    @PostMapping
    @Operation(summary = "Create a new bill and send WhatsApp notification")
    public ResponseEntity<Bill> createBill(@RequestBody BillDto billDto) {
        Bill created = billService.createBill(billDto);
        return ResponseEntity.ok(created);
    }

    @PostMapping("/mobile")
    @Operation(summary = "Create a bill for mobile users")
    public ResponseEntity<MobileBillResponse> createMobileBill(@RequestBody MobileBillRequest request) {
        MobileBillResponse response = billService.createMobileBill(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Fetch a bill by ID")
    public ResponseEntity<Bill> getBillById(@PathVariable Long id) {
        Bill bill = billService.getBillById(id);
        return ResponseEntity.ok(bill);
    }

    @GetMapping("/all")
    @Operation(summary = "Fetch all bills")
    public ResponseEntity<List<Bill>> getAllBills() {
        List<Bill> bills = billService.getAllBills();
        return ResponseEntity.ok(bills);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update an existing bill (partial fields)")
    public ResponseEntity<Bill> updateBill(@PathVariable Long id, @RequestBody BillDto billDto) {
        Bill updated = billService.updateBill(id, billDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a bill by ID")
    public ResponseEntity<String> deleteBill(@PathVariable Long id) {
        billService.deleteBill(id);
        return ResponseEntity.ok("Bill deleted successfully.");
    }

    // ***** FILTER ENDPOINTS *****
    
    @GetMapping("/bills")
    @Operation(summary = "Get bills by user ID")
    public ResponseEntity<List<Bill>> getBillsByUserId(@RequestParam Long userId) {
        List<Bill> bills = billService.getBillsByUserId(userId);
        return ResponseEntity.ok(bills);
    }

    @GetMapping("/bills/by-product")
    @Operation(summary = "Get bills by product name")
    public ResponseEntity<List<Bill>> getBillsByProductName(@RequestParam String productName) {
        List<Bill> bills = billService.getBillsByProductName(productName);
        return ResponseEntity.ok(bills);
    }

    @GetMapping("/bills/by-date-range")
    @Operation(summary = "Get bills by date range and user ID")
    public ResponseEntity<List<Bill>> getBillsByDateRange(
            @RequestParam Long userId,
            @RequestParam String from,
            @RequestParam String to) {
        List<Bill> bills = billService.getBillsByDateRange(userId, from, to);
        return ResponseEntity.ok(bills);
    }

    @GetMapping("/admin/bills/stats")
    @Operation(summary = "Get bill statistics")
    public ResponseEntity<BillStatsDto> getBillStats(@RequestParam String type) {
        BillStatsDto stats = billService.getBillStats(type);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/download-invoice/{billId}")
    @Operation(summary = "Download the invoice PDF for a given bill ID")
    public ResponseEntity<Resource> downloadInvoice(@PathVariable Long billId) {
        try {
            // Get relative file path from service
            String relativePath = invoiceService.generateInvoicePdf(billId);

            // Convert to absolute path
            Path filePath = Paths.get("").toAbsolutePath().resolve(relativePath).normalize();
            System.out.println("Invoice file path: " + filePath); // Debugging

            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/pdf";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
