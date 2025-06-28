package com.hotelapi.exception;

/**
 * Thrown when a bill with the specified ID is not found in the database.
 */
public class BillNotFoundException extends RuntimeException {

    // Constructor with custom message
    public BillNotFoundException(String message) {
        super(message);
    }

    // Optional: default constructor with generic message
    public BillNotFoundException() {
        super("Bill not found");
    }
}
