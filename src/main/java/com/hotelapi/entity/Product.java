package com.hotelapi.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

/**
 * Represents a product sold by the hotel
 */
@Entity
@Table(name = "products")
@Data // Lombok: generates getters, setters, equals, hashCode, toString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Represents a product sold by the hotel")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique product ID", example = "1")
    private Long id;

    @Column(nullable = false, length = 100, unique = true)
    @Schema(description = "Unique product code", example = "PTK001")
    private String productCode;

    @Column(nullable = false, length = 100)
    @Schema(description = "Name of the product", example = "Paneer Tikka")
    private String productName;

    @Schema(description = "Unit price of the product", example = "250.00")
    private Double price;

    @Schema(description = "Category of the product", example = "Food")
    private String category;

    @Schema(description = "Available quantity", example = "10")
    private Integer quantity;

    @Schema(description = "Active status of the product", example = "true")
    private Boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id")
    @ToString.Exclude
    @Schema(description = "Hotel that owns this product")
    private Hotel hotel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    @Schema(description = "User who owns this product")
    private User user;

}
