package com.hotelapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import lombok.Data;

@Data
@Schema(description = "Payload for creating or updating a product")
public class ProductRequest {

    @NotBlank
    @Size(max = 100)
    @Schema(description = "Name of the product", example = "Paneer Tikka")
    private String productName;

    @NotBlank
    @Size(max = 50)
    @Schema(description = "Unique product code", example = "PTK001")
    private String productCode;

    @NotBlank
    @Schema(description = "Product category", example = "Food")
    private String category;

    @NotNull
    @Min(0)
    @Schema(description = "Available quantity", example = "10")
    private Integer quantity;

    @NotNull
    @Positive
    @Schema(description = "Unit price of the product", example = "250.00")
    private Double price;

    @NotNull
    @Schema(description = "Whether the product is active", example = "true")
    private Boolean isActive;
}
