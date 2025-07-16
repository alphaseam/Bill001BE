package com.hotelapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * UserDto represents a user in the system.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO representing a user")
public class UserDto {

    @Schema(description = "Email of the user", example = "user@example.com")
    private String email;
}
