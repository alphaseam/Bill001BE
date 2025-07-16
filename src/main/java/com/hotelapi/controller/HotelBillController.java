package com.hotelapi.controller;

import com.hotelapi.dto.BillDto;
import com.hotelapi.dto.BillStatsDto;
import com.hotelapi.entity.Bill;
import com.hotelapi.service.BillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**

 * TICKET 3: HOTEL BILL MANAGEMENT CONTROLLER
 * This controller handles all CRUD operations and filtering for bills
 * as per the requirements specified in Ticket 3.
 * 
 * Required Endpoints:
 * - POST /api/hotel/bills - Create a new bill
 * - GET /api/hotel/bills?userId=123 - Get bills by user ID
 * - GET /api/hotel/bills/by-product?productName=Water Bottle - Filter by product
 * - GET /api/hotel/bills/by-date-range?userId=123&from=2025-07-01&to=2025-07-10 - Filter by date range
 * - GET /api/hotel/bills/{billId} - Get bill by ID
 * - PUT /api/hotel/bills/{billId} - Update bill (price, quantity, discount)
 * - DELETE /api/hotel/bills/{billId} - Delete bill
 */
@RestController
@RequestMapping("/api/hotel/bills")
@RequiredArgsConstructor
@Tag(name = "Hotel Bill Management", description = "CRUD operations and filtering for hotel bills")
public class HotelBillController {

    private final BillService billService;

    /**
     * CREATE BILL - POST /api/hotel/bills
     * Creates a new bill with the provided bill details.
     * Automatically calculates subtotal, tax, and total based on items.
     */
    @PostMapping
    @Operation(summary = "Create a new bill", 
               description = "Creates a new bill with items and calculates totals automatically")
    public ResponseEntity<Bill> createBill(
            @RequestBody BillDto billDto
    ) {
        Bill createdBill = billService.createBill(billDto);
        return ResponseEntity.ok(createdBill);
    }

    /**
     * GET BILLS BY USER - GET /api/hotel/bills?userId=123
     * Retrieves all bills associated with a specific user ID.
     */
    @GetMapping
    @Operation(summary = "Get bills by user ID", 
               description = "Retrieves all bills for a specific user")
    public ResponseEntity<List<Bill>> getBillsByUserId(
            @Parameter(description = "User ID to filter bills", example = "123")
            @RequestParam Long userId
    ) {
        List<Bill> bills = billService.getBillsByUserId(userId);
        return ResponseEntity.ok(bills);
    }

    /**
     * GET BILLS BY PRODUCT - GET /api/hotel/bills/by-product?productName=Water Bottle
     * Filters bills that contain items with the specified product name.
     */
    @GetMapping("/by-product")
    @Operation(summary = "Get bills by product name", 
               description = "Filters bills containing items with the specified product name")
    public ResponseEntity<List<Bill>> getBillsByProductName(
            @Parameter(description = "Product name to filter bills", example = "Water Bottle")
            @RequestParam String productName
    ) {
        List<Bill> bills = billService.getBillsByProductName(productName);
        return ResponseEntity.ok(bills);
    }

    /**
     * GET BILLS BY DATE RANGE - GET /api/hotel/bills/by-date-range?userId=123&from=2025-07-01&to=2025-07-10
     * Filters bills within a specific date range for a given user.
     */
    @GetMapping("/by-date-range")
    @Operation(summary = "Get bills by date range", 
               description = "Filters bills within a specific date range for a user")
    public ResponseEntity<List<Bill>> getBillsByDateRange(
            @Parameter(description = "User ID", example = "123")
            @RequestParam Long userId,
            @Parameter(description = "Start date (yyyy-MM-dd)", example = "2025-07-01")
            @RequestParam String from,
            @Parameter(description = "End date (yyyy-MM-dd)", example = "2025-07-10")
            @RequestParam String to
    ) {
        List<Bill> bills = billService.getBillsByDateRange(userId, from, to);
        return ResponseEntity.ok(bills);
    }

    /**
     * GET BILL BY ID - GET /api/hotel/bills/{billId}
     * Retrieves a specific bill by its ID.
     */
    @GetMapping("/{billId}")
    @Operation(summary = "Get bill by ID", 
               description = "Retrieves a specific bill by its unique ID")
    public ResponseEntity<Bill> getBillById(
            @Parameter(description = "Bill ID", example = "1")
            @PathVariable Long billId
    ) {
        Bill bill = billService.getBillById(billId);
        return ResponseEntity.ok(bill);
    }

    /**
     * UPDATE BILL - PUT /api/hotel/bills/{billId}
     * Updates an existing bill including price, quantity, and discount.
     * Automatically recalculates subtotal, tax, and total.
     */
    @PutMapping("/{billId}")
    @Operation(summary = "Update bill", 
               description = "Updates bill details including item prices, quantities, and discounts")
    public ResponseEntity<Bill> updateBill(
            @Parameter(description = "Bill ID to update", example = "1")
            @PathVariable Long billId,
            @RequestBody BillDto billDto
    ) {
        Bill updatedBill = billService.updateBill(billId, billDto);
        return ResponseEntity.ok(updatedBill);
    }

    /**
     * DELETE BILL - DELETE /api/hotel/bills/{billId}
     * Deletes a specific bill by its ID.
     */
    @DeleteMapping("/{billId}")
    @Operation(summary = "Delete bill", 
               description = "Permanently deletes a bill by its ID")
    public ResponseEntity<String> deleteBill(
            @Parameter(description = "Bill ID to delete", example = "1")
            @PathVariable Long billId
    ) {
        billService.deleteBill(billId);
        return ResponseEntity.ok("Bill deleted successfully");
    }
}
