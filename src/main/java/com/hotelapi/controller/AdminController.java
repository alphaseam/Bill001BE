package com.hotelapi.controller;

import com.hotelapi.dto.BillStatsDto;
import com.hotelapi.service.BillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * ============================================================================
 * ADMIN CONTROLLER FOR ANALYTICS
 * ============================================================================
 * This controller handles admin-specific operations including analytics
 * and statistics for bills.
 * 
 * Required Endpoint:
 * - GET /admin/bills/stats?type=monthly|weekly|productwise
 * ============================================================================
 */
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Tag(name = "Admin Analytics", description = "Admin operations and analytics")
public class AdminController {

    private final BillService billService;

    /**
     * ========================================================================
     * GET BILL STATISTICS - GET /admin/bills/stats?type=monthly|weekly|productwise
     * ========================================================================
     * Provides comprehensive analytics for bills based on the specified type.
     * 
     * Analytics Types:
     * - monthly: Groups bills by month and provides monthly statistics
     * - weekly: Groups bills by week and provides weekly statistics  
     * - productwise: Groups bills by product and provides product-wise statistics
     */
    @GetMapping("/bills/stats")
    @Operation(summary = "Get bill statistics", 
               description = "Provides comprehensive analytics for bills based on type (monthly, weekly, or productwise)")
    public ResponseEntity<BillStatsDto> getBillStats(
            @Parameter(description = "Type of statistics to generate", 
                      example = "monthly", 
                      schema = @io.swagger.v3.oas.annotations.media.Schema(
                          type = "string", 
                          allowableValues = {"monthly", "weekly", "productwise"}))
            @RequestParam String type
    ) {
        BillStatsDto stats = billService.getBillStats(type);
        return ResponseEntity.ok(stats);
    }
}
