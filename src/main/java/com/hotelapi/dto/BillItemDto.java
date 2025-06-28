package com.hotelapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * BillItemDto represents a single item entry in a bill.
 * It contains product ID, quantity, and unit price.
 */
@Data // Lombok: generates getters, setters, toString, equals, and hashCode
@NoArgsConstructor // Lombok: generates a no-argument constructor
@AllArgsConstructor // Lombok: generates an all-arguments constructor
@Schema(description = "Represents a single item in a bill")
public class BillItemDto {

    @Schema(description = "ID of the product", example = "101")
    private Long productId;

    @Schema(description = "Quantity of the product purchased", example = "2")
    private Integer quantity;

    @Schema(description = "Price per unit of the product", example = "299.99")
    private Double price;

}
