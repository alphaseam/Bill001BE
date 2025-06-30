package com.hotelapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Response details of a product")
public class ProductResponse {

    @Schema(description = "Unique product ID", example = "1")
    private Long id;

    @Schema(description = "Product name", example = "Paneer Tikka")
    private String productName;

    @Schema(description = "Unique product code", example = "PTK001")
    private String productCode;

    @Schema(description = "Category of the product", example = "Food")
    private String category;

    @Schema(description = "Available quantity", example = "10")
    private Integer quantity;

    @Schema(description = "Unit price of the product", example = "250.00")
    private Double price;

    @Schema(description = "Whether the product is active", example = "true")
    private Boolean isActive;

    @Schema(description = "Hotel ID to which this product belongs", example = "1")
    private Long hotelId;
}
