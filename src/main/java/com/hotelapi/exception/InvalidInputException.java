package com.hotelapi.exception;

/**
 * Thrown when input validation fails during entity creation or update.
 */
public class InvalidInputException extends RuntimeException {

    // Constructor with custom message
    public InvalidInputException(String message) {
        super(message);
    }

}
