
package com.hotelapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Request to reset password using verified OTP")
public class ResetPasswordRequest {

    @NotBlank(message = "Email is required")
    @Schema(description = "User's registered email", example = "user@example.com", required = true)
    private String email;

    @NotBlank(message = "OTP is required")
    @Schema(description = "OTP received and verified", example = "123456", required = true)
    private String otp;

    @NotBlank(message = "New password is required")
    @Schema(description = "New password to be set", example = "StrongPassword@123", required = true)
    private String newPassword;


}
