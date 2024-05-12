package com.cardio_generator.outputs;

/**
 * Interface for handling the output of generated health data.
 * Implementations of this interface define methods to process and output data
 * in various formats,
 * depending on the specific needs of the application.
 */

public interface OutputStrategy {
    /**
     * Processes and outputs the data for a specific patient.
     *
     * @param patientId the unique identifier of the patient
     * @param timestamp the time at which the data is recorded
     * @param label     a label describing the type of data
     * @param data      the actual data to be output
     */
    void output(int patientId, long timestamp, String label, String data);
}