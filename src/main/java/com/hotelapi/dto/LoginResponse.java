package com.hotelapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@Data
@AllArgsConstructor
@Schema(description = "Response after login attempt")
public class LoginResponse {

    @Schema(description = "Login status", example = "true")
    private boolean success;

    @Schema(description = "Login message", example = "Login successful")
    private String message;

    @Schema(description = "JWT access token", example = "fewafbwekhqfbkhqbewfhbekqwjh...")
    private String accessToken;

    @Schema(description = "JWT refresh token", example = "dwne djkn wekjf kwejnf kj...")
    private String refreshToken;
}
