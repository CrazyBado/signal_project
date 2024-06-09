package com.alerts;

public class Alert implements Comparable<Alert> {
    private String patientId;
    private String condition;
    private long timestamp;

    public Alert(String patientId, String condition, long timestamp) {
        if (patientId == null || patientId.isEmpty()) {
            throw new IllegalArgumentException("Patient ID cannot be null or empty");
        }
        if (condition == null || condition.isEmpty()) {
            throw new IllegalArgumentException("Condition cannot be null or empty");
        }
        if (timestamp <= 0) {
            throw new IllegalArgumentException("Timestamp must be positive");
        }
        this.patientId = patientId;
        this.condition = condition;
        this.timestamp = timestamp;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getCondition() {
        return condition;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Alert{" +
                "patientId='" + patientId + '\'' +
                ", condition='" + condition + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }

    @Override
    public int compareTo(Alert other) {
        return Long.compare(this.timestamp, other.timestamp);
    }
}

