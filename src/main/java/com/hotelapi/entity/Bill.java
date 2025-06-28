package com.hotelapi.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name = "bills")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Billing information for a transaction")
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Primary key for the bill

    @Schema(description = "Bill number or invoice ID", example = "INV-1001")
    private String billNumber; // Optional invoice number (can be auto-generated)

    @Schema(description = "Date and time of billing")
    private LocalDateTime createdAt; // Timestamp when the bill was created

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @Schema(description = "Customer associated with this bill")
    private Customer customer; // Linked customer entity

    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL, orphanRemoval = true)
    @Schema(description = "List of items in the bill")
    private List<BillItem> items; // List of items included in the bill

    @Schema(description = "Subtotal of items", example = "1200.00")
    private Double subtotal; // Sum of all item prices before tax and discount

    @Schema(description = "Applied tax amount", example = "144.00")
    private Double tax; // Total tax applied

    @Schema(description = "Discount amount if any", example = "50.00")
    private Double discount; // Discount on total (if applicable)

    @Schema(description = "Final grand total", example = "1294.00")
    private Double total; // Final amount = subtotal + tax - discount
}
