package com.hotelapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * GenericResponse is a reusable wrapper for API responses.
 * It helps in sending consistent response formats from all controllers.
 */
@Data                       // Lombok: Generates getters, setters, toString, equals, hashCode
@NoArgsConstructor          // Lombok: Generates a no-arg constructor
@AllArgsConstructor         // Lombok: Generates a constructor with all fields
@Schema(description = "Standard API response wrapper")
public class GenericResponse<T> {

    @Schema(description = "Response message", example = "Request successful")
    private String message;         // Descriptive message like "Bill created successfully"

    @Schema(description = "Success flag", example = "true")
    private boolean success;        // Flag to indicate whether the operation was successful

    @Schema(description = "Optional payload data")
    private T data;                 // Actual data

    /**
     * Constructor for response without data.
     * Useful for errors or simple success messages.
     */
    public GenericResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    /**
     * Static method for successful responses.
     */
    public static <T> GenericResponse<T> ok(String message, T data) {
        return new GenericResponse<>(message, true, data);
    }

    /**
     * Static method for failure responses.
     */
    public static <T> GenericResponse<T> fail(String message) {
        return new GenericResponse<>(message, false, null);
    }
}
