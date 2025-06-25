package com.hotelapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Standard API response wrapper")
public class ResponseDto<T> {

    @Schema(description = "Response message", example = "Request successful")
    private String message;

    @Schema(description = "Success flag", example = "true")
    private boolean success;

    @Schema(description = "Optional payload data")
    private T data;

    public ResponseDto(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public static <T> ResponseDto<T> ok(String message, T data) {
        return new ResponseDto<>(message, true, data);
    }

    public static <T> ResponseDto<T> fail(String message) {
        return new ResponseDto<>(message, false, null);
    }
}
