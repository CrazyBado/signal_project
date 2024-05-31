package com.alerts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

/**
 * Monitors patient data and generates alerts when predefined conditions are
 * met.
 * Relies on {@link DataStorage} to access and evaluate patient data against
 * health criteria.
 */
public class AlertGenerator {
    private DataStorage dataStorage;
    private Map<Integer, Map<String, Double>> patientAlertThresholds;

    /**
     * Constructs an AlertGenerator with specified DataStorage.
     * DataStorage is used to retrieve and monitor patient data.
     *
     * @param dataStorage the data storage system providing access to patient data
     */
    public AlertGenerator(DataStorage dataStorage) {
        this.dataStorage = dataStorage; // Ensured consistent spacing around "="
        this.patientAlertThresholds = new HashMap<>();
    }

    /**
     * Evaluates a patient's data to determine if alert conditions are met.
     * Triggers an alert if a condition is met.
     *
     * @param patient the patient data to evaluate for alert conditions
     */
    public void evaluateData(Patient patient) {
        List<PatientRecord> recentRecords = patient.getRecords(System.currentTimeMillis() - 3600000,
                System.currentTimeMillis());
        Map<String, Double> thresholds = patientAlertThresholds.get(patient.getPatientId());
        for (PatientRecord patientRecord : recentRecords) {
            double threshold = thresholds.get(patientRecord.getRecordType());
            if ("HeartRate".equals(patientRecord.getRecordType()) && patientRecord.getMeasurementValue() > threshold) {
                triggerAlert(new Alert(patient.getPatientId(), "High Heart Rate", System.currentTimeMillis()));
            }
        }
    }

    public void setThreshold(int patientId, String condition, double value) {
        if (!patientAlertThresholds.containsKey(patientId)) {
            patientAlertThresholds.put(patientId, new HashMap<>());
        }
        patientAlertThresholds.get(patientId).put(condition, value);
    }

    /**
     * Triggers an alert for the monitoring system. Can be extended to notify
     * medical staff,
     * log the alert, or other actions. Assumes the alert information is fully
     * formed.
     *
     * @param alert the alert object with details about the alert condition
     */
    private void triggerAlert(Alert alert) {
        AlertManager.getInstance().sendAlert(alert);
    }
}
