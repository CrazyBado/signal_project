package com.alerts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.data_storage.DataStorage;
import com.data_storage.PatientData;

public class AlertGenerator {
    private DataStorage dataStorage;
    private Map<Integer, Map<String, Double>> patientAlertThresholds;

    public AlertGenerator(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
        this.patientAlertThresholds = new HashMap<>();
    }

    public void evaluateData(PatientData patient) {
        int patientId = Integer.parseInt(patient.getPatientId());
        if (!patientAlertThresholds.containsKey(patientId)) return;

        Map<String, Double> thresholds = patientAlertThresholds.get(patientId);
        for (Map.Entry<String, Double> entry : patient.getMetrics().entrySet()) {
            String metric = entry.getKey();
            double value = entry.getValue();
            if (thresholds.containsKey(metric) && value > thresholds.get(metric)) {
                triggerAlert(new Alert(patientId, metric, System.currentTimeMillis()));
            }
        }
    }

    public void setThreshold(int patientId, String condition, double value) {
        if (!patientAlertThresholds.containsKey(patientId)) {
            patientAlertThresholds.put(patientId, new HashMap<>());
        }
        patientAlertThresholds.get(patientId).put(condition, value);
    }

    private void triggerAlert(Alert alert) {
        AlertManager.getInstance().sendAlert(alert);
    }
}