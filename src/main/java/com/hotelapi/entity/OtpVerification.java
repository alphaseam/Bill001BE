
package com.hotelapi.entity;

import jakarta.persistence.*;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "otpverification")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entity to store OTP verification details for password recovery")
public class OtpVerification {

    @Id
    @GeneratedValue
    @Schema(description = "Unique identifier for OTP record")
    private UUID id;

    @Schema(description = "Email associated with the OTP", example = "user@example.com")
    private String email;

    @Schema(description = "OTP code sent to the user", example = "123456")
    private String otp;

    @Schema(description = "Time at which the OTP will expire")
    private LocalDateTime expiryTime;

    @Schema(description = "Indicates whether the OTP has been verified", example = "false")
    private boolean verified;

    @Schema(description = "Time when the OTP was created")
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

	
}
