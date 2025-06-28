package com.hotelapi.exception;

/**
 * Thrown when an update request contains invalid or unsupported fields.
 */
public class InvalidUpdateException extends RuntimeException {

    public InvalidUpdateException(String message) {
        super(message);
    }
}
