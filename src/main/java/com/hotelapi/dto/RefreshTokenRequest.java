package com.hotelapi.dto;

import lombok.Data;
import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@Data
public class RefreshTokenRequest {
    private String refreshToken;
}
