package com.hotelapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@Data
@Schema(description = "Request to register a new user")
public class RegisterRequest {

    @Schema(description = "User's email address", example = "user@example.com", required = true)
    private String email;

    @Schema(description = "User's password", example = "securePassword123", required = true)
    private String password;
}
