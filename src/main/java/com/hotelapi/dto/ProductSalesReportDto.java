package com.hotelapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Monthly product sales summary")
public class ProductSalesReportDto {

    @Schema(description = "Product ID", example = "101")
    private Long productId;

    @Schema(description = "Product name", example = "Paneer Tikka")
    private String productName;

    @Schema(description = "Total units sold", example = "120")
    private Long unitsSold;

    @Schema(description = "Total revenue generated", example = "24000.50")
    private Double totalRevenue;

    @Schema(description = "Product category (optional)", example = "Food")
    private String category;
}
