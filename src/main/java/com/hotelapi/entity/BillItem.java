package com.hotelapi.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name = "bill_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "An individual item in a bill")
public class BillItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Product or item name", example = "Paneer Butter Masala")
    private String itemName;

    @Schema(description = "Quantity ordered", example = "2")
    private Integer quantity;

    @Schema(description = "Unit price", example = "250.00")
    private Double unitPrice;

    @Schema(description = "Discount on this item", example = "10.00")
    private Double discount;

    @Schema(description = "Total price for this line item", example = "490.00")
    private Double total;

    @ManyToOne
    @JoinColumn(name = "bill_id", nullable = false)
    @JsonBackReference // Prevents infinite loop during serialization
    private Bill bill;
}
