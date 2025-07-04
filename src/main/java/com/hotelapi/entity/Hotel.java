package com.hotelapi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "hotels", uniqueConstraints = @UniqueConstraint(columnNames = "mobile"))
@Data                       // Generates getters, setters, toString, equals, hashCode
@NoArgsConstructor          // Required for JPA/Hibernate
@AllArgsConstructor         // Required for @Builder to work
@Builder                    // Enables builder pattern
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long hotelId;

    private String hotelName;
    private String ownerName;
    private String mobile;
    private String email;
    private String address;
    private String gstNumber;
    private String hotelType;
    private Boolean isActive;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
