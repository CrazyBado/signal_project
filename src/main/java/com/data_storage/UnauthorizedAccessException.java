package com.data_storage;

/**
 * Exception thrown when a user tries to access data they are not authorized to access.
 */
public class UnauthorizedAccessException extends Exception {
    /**
     * Constructs a new UnauthorizedAccessException with the specified detail message.
     *
     * @param message the detail message
     */
    public UnauthorizedAccessException(String message) {
        super(message);
    }
}
