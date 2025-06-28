package com.hotelapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Request to register a new user")
public class RegisterRequest {

    @Schema(description = "User's email address", example = "user@example.com", required = true)
    @Email
    @NotBlank
    private String email;

    @Schema(description = "User's password", example = "securePassword123", required = true)
    @NotBlank
    private String password;
}
