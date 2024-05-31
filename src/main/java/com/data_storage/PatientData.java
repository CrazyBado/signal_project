package com.data_storage;

import java.util.Map;

/**
 * Represents the data for a single patient.
 */
public class PatientData {
    private String patientId;
    private Map<String, Double> metrics;
    private long timestamp;

    /**
     * Constructs a new PatientData object.
     *
     * @param patientId the unique identifier for the patient
     * @param metrics   a map of health metrics for the patient
     * @param timestamp the time the data was recorded
     */
    public PatientData(String patientId, Map<String, Double> metrics, long timestamp) {
        this.patientId = patientId;
        this.metrics = metrics;
        this.timestamp = timestamp;
    }

    /**
     * Returns the patient ID.
     *
     * @return the patient ID
     */
    public String getPatientId() {
        return patientId;
    }

    /**
     * Sets the patient ID.
     *
     * @param patientId the patient ID
     */
    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    /**
     * Returns the health metrics.
     *
     * @return a map of health metrics
     */
    public Map<String, Double> getMetrics() {
        return metrics;
    }

    /**
     * Sets the health metrics.
     *
     * @param metrics a map of health metrics
     */
    public void setMetrics(Map<String, Double> metrics) {
        this.metrics = metrics;
    }

    /**
     * Returns the timestamp.
     *
     * @return the timestamp
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Sets the timestamp.
     *
     * @param timestamp the timestamp
     */
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}