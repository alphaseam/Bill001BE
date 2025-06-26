package com.hotelapi.controller;

import com.hotelapi.entity.Bill;
import com.hotelapi.service.BillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bill")
@Tag(name = "Bill", description = "Bill creation and management")
@RequiredArgsConstructor
public class BillController {

    private final BillService billService;

    @PostMapping("/create")
    @Operation(summary = "Create a new bill and auto-send WhatsApp notification")
    public ResponseEntity<?> createBill(@RequestBody Bill bill) {
        try {
            Bill savedBill = billService.createBillWithNotification(bill);
            return ResponseEntity.ok(savedBill);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Bill creation failed: " + e.getMessage());
        }
    }
}
