package com.alerts;

/**
 * Represents an alert that is generated when a patient's health data crosses
 * predefined thresholds.
 * This class stores details about the alert such as the patient's ID, the
 * specific health condition that triggered the alert, and the time the alert
 * was generated.
 */
public class Alert {
    private int patientId;
    private String condition;
    private long timestamp;

    /**
     * Constructs a new Alert with specified details.
     *
     * @param patientId The unique identifier for the patient.
     * @param condition The health condition that triggered the alert.
     * @param timestamp The timestamp when the alert was generated, typically the
     *                  time when the condition was detected.
     */
    public Alert(int patientId, String condition, long timestamp) {
        this.patientId = patientId;
        this.condition = condition;
        this.timestamp = timestamp;
    }

    /**
     * Returns the patient ID associated with this alert.
     * 
     * @return the patient ID.
     */
    public int getPatientId() {
        return patientId;
    }

    /**
     * Returns the condition that triggered this alert.
     * 
     * @return the condition description.
     */
    public String getCondition() {
        return condition;
    }

    /**
     * Returns the timestamp when the alert was generated.
     * 
     * @return the timestamp as a long.
     */
    public long getTimestamp() {
        return timestamp;
    }
}
