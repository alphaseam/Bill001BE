package com.hotelapi.exception;

/**
 * Thrown when input validation fails during bill creation or update.
 */
public class InvalidInputException extends RuntimeException {

    // Constructor with custom message
    public InvalidInputException(String message) {
        super(message);
    }

}
