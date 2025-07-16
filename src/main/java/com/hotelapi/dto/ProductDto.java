package com.hotelapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ProductDto represents a product in the system.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO representing a product")
public class ProductDto {

    @Schema(description = "Product code", example = "PTK001")
    private String productCode;

    @Schema(description = "Product name", example = "Paneer Tikka")
    private String productName;

    @Schema(description = "Unit price", example = "250.00")
    private Double price;

    @Schema(description = "Category", example = "Food")
    private String category;

    @Schema(description = "Available quantity", example = "10")
    private Integer quantity;

    @Schema(description = "Active status", example = "true")
    private Boolean isActive;
}
