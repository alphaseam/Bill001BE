package com.hotelapi.controller;

import com.hotelapi.dto.BillDto;
import com.hotelapi.entity.Bill;
import com.hotelapi.service.BillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bill")
@Tag(name = "Bill", description = "Bill creation and management")
@RequiredArgsConstructor
public class BillController {

    private final BillService billService;

    /**
     * JIRA-101: Create a new bill
     */
    @PostMapping
    @Operation(summary = "Create a new bill and send WhatsApp notification")
    public ResponseEntity<Bill> createBill(@RequestBody BillDto billDto) {
        Bill created = billService.createBill(billDto);
        return ResponseEntity.ok(created);
    }

    /**
     * JIRA-102: Get a bill by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Fetch a bill by ID")
    public ResponseEntity<Bill> getBillById(@PathVariable Long id) {
        Bill bill = billService.getBillById(id);
        return ResponseEntity.ok(bill);
    }

    /**
     * JIRA-103: Get all bills
     */
    @GetMapping("/all")
    @Operation(summary = "Fetch all bills")
    public ResponseEntity<List<Bill>> getAllBills() {
        List<Bill> bills = billService.getAllBills();
        return ResponseEntity.ok(bills);
    }

    /**
     * JIRA-104: Partially update a bill
     */
    @PatchMapping("/{id}")
    @Operation(summary = "Update an existing bill (partial fields)")
    public ResponseEntity<Bill> updateBill(@PathVariable Long id, @RequestBody BillDto billDto) {
        Bill updated = billService.updateBill(id, billDto);
        return ResponseEntity.ok(updated);
    }

    /**
     * JIRA-105: Delete a bill by ID
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a bill by ID")
    public ResponseEntity<String> deleteBill(@PathVariable Long id) {
        billService.deleteBill(id);
        return ResponseEntity.ok("Bill deleted successfully.");
    }
}
