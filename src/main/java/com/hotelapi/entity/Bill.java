package com.hotelapi.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

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
    private Long id;

    @Schema(description = "Bill number or invoice ID", example = "INV-1001")
    private String billNumber;

    @Schema(description = "Date and time of billing")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @Schema(description = "Customer associated with this bill")
    private Customer customer;

    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @Schema(description = "List of items in the bill")
    private List<BillItem> items;

    @Schema(description = "Subtotal of items", example = "1200.00")
    private Double subtotal;

    @Schema(description = "Applied tax amount", example = "144.00")
    private Double tax;

    @Schema(description = "Discount amount if any", example = "50.00")
    private Double discount;

    @Schema(description = "Final grand total", example = "1294.00")
    private Double total;
}
