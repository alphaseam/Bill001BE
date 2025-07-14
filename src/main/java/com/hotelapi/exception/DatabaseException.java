package com.hotelapi.exception;

/**
 * Custom exception to indicate database-related errors such as failed saves, updates, etc.
 */
public class DatabaseException extends RuntimeException {

    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
