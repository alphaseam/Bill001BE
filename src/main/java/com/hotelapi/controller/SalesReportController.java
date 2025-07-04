package com.hotelapi.controller;

import com.hotelapi.dto.ProductSalesReportResponse;
import com.hotelapi.dto.GenericResponse;
import com.hotelapi.dto.SalesReportResponse;
import com.hotelapi.service.SalesReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports/sales")
@RequiredArgsConstructor
@Tag(name = "Sales Reports", description = "Endpoints for sales reporting")
public class SalesReportController {

    private final SalesReportService salesReportService;

    @Operation(summary = "Get monthly product-wise sales report", description = "Accessible to ADMIN and MANAGER roles only")
    @GetMapping("/monthly/product-wise")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public GenericResponse<List<ProductSalesReportResponse>> getMonthlyProductWiseReport(
            @Parameter(description = "Month (1-12)", example = "6")
            @RequestParam Integer month,
            @Parameter(description = "Year (e.g., 2025)", example = "2025")
            @RequestParam Integer year
    ) {
        return salesReportService.getMonthlyProductSalesReport(month, year);
    }

    @Operation(summary = "Get daily sales report", description = "Accessible to ADMIN and MANAGER roles only")
    @GetMapping("/daily")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public SalesReportResponse getDailySalesReport(
            @Parameter(description = "From date (yyyy-MM-dd)", example = "2025-06-01")
            @RequestParam(required = false) String fromDate,
            @Parameter(description = "To date (yyyy-MM-dd)", example = "2025-06-30")
            @RequestParam(required = false) String toDate
    ) {
        return salesReportService.getDailySales(fromDate, toDate);
    }

    @Operation(summary = "Get monthly overall sales report", description = "Accessible to ADMIN and MANAGER roles only")
    @GetMapping("/monthly")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public SalesReportResponse getMonthlySalesReport(
            @Parameter(description = "Year (e.g., 2025)", example = "2025")
            @RequestParam(required = false) Integer year
    ) {
        return salesReportService.getMonthlySales(year);
    }

    @Operation(summary = "Get yearly sales report", description = "Accessible to ADMIN and MANAGER roles only")
    @GetMapping("/yearly")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public SalesReportResponse getYearlySalesReport(
            @Parameter(description = "From year", example = "2020")
            @RequestParam(required = false) Integer fromYear,
            @Parameter(description = "To year", example = "2025")
            @RequestParam(required = false) Integer toYear
    ) {
        return salesReportService.getYearlySales(fromYear, toYear);
    }
}
