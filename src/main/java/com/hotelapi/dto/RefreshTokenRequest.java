package com.hotelapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Request payload for refreshing JWT token")
public class RefreshTokenRequest {

    @Schema(
        description = "The refresh token issued during login",
        example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
        required = true
    )
    private String refreshToken;
}
