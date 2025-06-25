package com.hotelapi.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Order placed by customer for a specific product")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique order ID", example = "1001")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @Schema(description = "Product associated with the order")
    @ToString.Exclude
    private Product product;

    @Column(nullable = false)
    @Schema(description = "Quantity of product ordered", example = "3")
    private Integer quantity;

    @Column(nullable = false)
    @Schema(description = "Total price for the ordered quantity", example = "750.0")
    private Double totalPrice;

    @Column(nullable = false)
    @Schema(description = "Order status", example = "COMPLETED")
    private String status;

    @Column(name = "created_at", nullable = false)
    @Schema(description = "Date and time of order placement", example = "2025-06-01T14:30:00")
    private LocalDateTime createdAt;
}
