package com.hotelapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * BillDto is the main data transfer object used to create or return a bill.
 * It contains customer reference, list of items, discount, total, and date.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO representing a full bill including customer and item details")
public class BillDto {

//    @Schema(description = "ID of the customer making the purchase", example = "501")
//    private Long customerId;

	 @Schema(description = "Full name of the customer", example = "Rahul Patil", required = true)
	 private String customerName;

	 @Schema(description = "Mobile number of the customer", example = "9876543210", required = true)
	 private String customerMobile;
	    
    @Schema(description = "ID of the hotel for this bill", example = "1")
    private Long hotelId;

    @Schema(description = "List of items included in this bill")
    private List<BillItemDto> items;

    @Schema(description = "Discount amount applied to the total bill", example = "50.0")
    private Double discount;

    @Schema(description = "Subtotal before tax and discount", example = "1000.0")
    private Double subtotal;

    @Schema(description = "Tax amount applied to the bill", example = "120.0")
    private Double tax;

    @Schema(description = "Final total after applying tax and discount", example = "1070.0")
    private Double total;

    @Schema(description = "Date the bill was generated", example = "2025-06-28")
    private LocalDate billDate;

    @Schema(description = "Any optional notes or remarks for the bill", example = "Lunch order for table 4")
    private String remarks;
}
