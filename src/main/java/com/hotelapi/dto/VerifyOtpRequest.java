package com.hotelapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Request to verify OTP received via email")
public class VerifyOtpRequest {

    @NotBlank(message = "Email is required")
    @Schema(description = "User's registered email", example = "user@example.com", required = true)
    private String email;

    @NotBlank(message = "OTP is required")
    @Schema(description = "OTP received by user", example = "123456", required = true)
    private String otp;
}
