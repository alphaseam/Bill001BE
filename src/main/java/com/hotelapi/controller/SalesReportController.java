package com.hotelapi.controller;

import com.hotelapi.dto.ProductSalesReportDto;
import com.hotelapi.dto.ResponseDto;
import com.hotelapi.service.SalesReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports/sales/monthly")
@RequiredArgsConstructor
@Tag(name = "Sales Reports", description = "Endpoints for monthly product-wise sales reporting")
public class SalesReportController {

    private final SalesReportService salesReportService;

    @Operation(summary = "Get monthly product-wise sales report", description = "Accessible to ADMIN and MANAGER roles only")
    @GetMapping("/product-wise")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseDto<List<ProductSalesReportDto>> getMonthlyReport(
            @Parameter(description = "Month (1-12)", example = "6")
            @RequestParam Integer month,

            @Parameter(description = "Year (e.g., 2025)", example = "2025")
            @RequestParam Integer year
    ) {
        return salesReportService.getMonthlyProductSalesReport(month, year);
    }
}
