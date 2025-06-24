package com.hotelapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "User registration form")
public class RegisterForm {

    @Schema(description = "Full name of the user", example = "Abhishek Mahajan", required = true)
    @NotBlank
    private String name;

    @Schema(description = "User's email address", example = "abhishek@example.com", required = true)
    @Email
    @NotBlank
    private String email;

    @Schema(description = "Password (min 6 characters recommended)", example = "StrongPassword123!", required = true)
    @NotBlank
    private String password;
}
