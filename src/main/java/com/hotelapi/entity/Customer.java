package com.hotelapi.entity;

import jakarta.persistence.*;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name = "customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Customer information for billing")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Customer's full name", example = "aman das")
    private String name;

    @Schema(description = "Customer's mobile number", example = "9876543210")
    private String mobile;

    @Schema(description = "Customer's email address", example = "aman@gmail.com")
    private String email;

    @Schema(description = "Optional address")
    private String address;
}
