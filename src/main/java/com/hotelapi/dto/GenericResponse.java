package com.hotelapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Standard API response wrapper")
public class GenericResponse<T> {

    @Schema(description = "Response message", example = "Request successful")
    private String message;

    @Schema(description = "Success flag", example = "true")
    private boolean success;

    @Schema(description = "Optional payload data")
    private T data;

    public GenericResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public static <T> GenericResponse<T> ok(String message, T data) {
        return new GenericResponse<>(message, true, data);
    }

    public static <T> GenericResponse<T> fail(String message) {
        return new GenericResponse<>(message, false, null);
    }
}
