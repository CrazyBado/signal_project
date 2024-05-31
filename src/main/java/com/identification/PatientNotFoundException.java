package com.identification;

/**
 * Exception thrown when a patient ID is not found in the database.
 */
public class PatientNotFoundException extends Exception {
    /**
     * Constructs a new PatientNotFoundException with the specified detail message.
     *
     * @param message the detail message
     */
    public PatientNotFoundException(String message) {
        super(message);
    }
}