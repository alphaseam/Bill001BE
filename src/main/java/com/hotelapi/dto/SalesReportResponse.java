package com.hotelapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesReportResponse {
    private BigDecimal totalRevenue;
    private Long totalTransactions;
    private String bestSellingProduct;
    private String topCustomer;
}
