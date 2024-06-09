package com.cardio_generator.outputs;

/**
 * Implements the OutputStrategy interface for writing health data to the
 * console.
 * This strategy outputs data directly to the standard output.
 */
public class ConsoleOutputStrategy implements OutputStrategy {

    /**
     * Outputs data to the console.
     *
     * @param patientId the unique identifier of the patient
     * @param timestamp the time at which the data is recorded, in milliseconds
     *                  since the epoch
     * @param label     a label describing the type of data (e.g., "HeartRate",
     *                  "BloodPressure")
     * @param data      the actual data to be output as a string
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        System.out.printf("Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n", patientId, timestamp, label, data);
    }
}
