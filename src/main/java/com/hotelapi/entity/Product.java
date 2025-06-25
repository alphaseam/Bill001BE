package com.hotelapi.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Represents a product sold by the hotel")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique product ID", example = "1")
    private Long id;

    @Column(nullable = false, length = 100)
    @Schema(description = "Name of the product", example = "Paneer Tikka")
    private String name;

    @Schema(description = "Unit price of the product", example = "250.00")
    private Double price;

    @Schema(description = "Category of the product", example = "Food")
    private String category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id")
    @ToString.Exclude
    @Schema(description = "Hotel that owns this product")
    private Hotel hotel;
}
