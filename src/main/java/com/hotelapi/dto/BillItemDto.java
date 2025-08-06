package com.hotelapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * BillItemDto represents a single item entry in a bill.
 * It contains product ID, name, quantity, unit price, discount, and total.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Represents a single item in a bill")
public class BillItemDto {

    @Schema(description = "ID of the product", example = "101")
    private Long productId;

    @Schema(description = "Name of the product", example = "Paneer Tikka")
    private String itemName;

    @Schema(description = "Quantity of the product purchased", example = "2")
    private Integer quantity;

    @Schema(description = "Price per unit of the product", example = "299.99")
    private Double price;

    @Schema(description = "Discount applied to the item", example = "10.0")
    private Double discount;

    @Schema(description = "Total amount for this item", example = "589.98")
    private Double total;
    

}

