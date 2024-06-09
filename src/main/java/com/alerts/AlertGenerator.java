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
        String patientId = patient.getPatientId();
        System.out.println("Evaluating data for patient: " + patientId + " " + patient.getMetrics());
        checkBloodPressureAlerts(patientId, patient.getMetrics());
        checkBloodSaturationAlerts(patientId, patient.getMetrics());
        checkECGDataAlerts(patientId, patient.getMetrics());
        checkHypotensiveHypoxemiaAlerts(patientId, patient.getMetrics());
    }

    public void setThreshold(int patientId, String condition, double value) {
        patientAlertThresholds
            .computeIfAbsent(patientId, k -> new HashMap<>())
            .put(condition, value);
    }

    private void checkBloodPressureAlerts(String patientId, Map<String, Double> metrics) {
        Double systolic = metrics.get("SystolicPressure");
        Double diastolic = metrics.get("DiastolicPressure");

        if (systolic == null || diastolic == null) {
            return;
        }

        if (systolic > 180 || systolic < 90 || diastolic > 120 || diastolic < 60) {
            triggerAlert(new Alert(patientId, "Critical Blood Pressure", System.currentTimeMillis()));
        }

        checkTrends(patientId, "SystolicPressure", systolic, "Blood Pressure Increasing Trend", "Blood Pressure Decreasing Trend");
        checkTrends(patientId, "DiastolicPressure", diastolic, "Blood Pressure Increasing Trend", "Blood Pressure Decreasing Trend");
    }

    private void checkBloodSaturationAlerts(String patientId, Map<String, Double> metrics) {
        Double saturation = metrics.get("Saturation");
        if (saturation == null) {
            return;
        }

        if (saturation < 92) {
            triggerAlert(new Alert(patientId, "Low Blood Oxygen Saturation", System.currentTimeMillis()));
        }

        List<Double> saturationReadings = dataStorage.getRecentReadings(patientId, "Saturation", 10);
        if (saturationReadings.size() >= 2) {
            double latest = saturationReadings.get(saturationReadings.size() - 1);
            double previous = saturationReadings.get(saturationReadings.size() - 2);
            if (previous - latest >= 5) {
                triggerAlert(new Alert(patientId, "Rapid Drop in Blood Oxygen Saturation", System.currentTimeMillis()));
            }
        }
    }

    private void checkHypotensiveHypoxemiaAlerts(String patientId, Map<String, Double> metrics) {
        Double systolic = metrics.get("SystolicPressure");
        Double saturation = metrics.get("Saturation");

        if (systolic == null || saturation == null) {
            return;
        }

        if (systolic < 90 && saturation < 92) {
            triggerAlert(new Alert(patientId, "Hypotensive Hypoxemia", System.currentTimeMillis()));
        }
    }

    private void checkECGDataAlerts(String patientId, Map<String, Double> metrics) {
        Double ecg = metrics.get("ECG");
        if (ecg == null) {
            return;
        }

        List<Double> ecgReadings = dataStorage.getRecentReadings(patientId, "ECG", 5);
        if (ecgReadings.size() < 5) {
            return;
        }

        double average = ecgReadings.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        if (ecg > average * 1.5) {
            triggerAlert(new Alert(patientId, "Abnormal ECG Data", System.currentTimeMillis()));
        }
    }

    private void checkTrends(String patientId, String metric, double value, String increasingAlert, String decreasingAlert) {
        List<Double> readings = dataStorage.getRecentReadings(patientId, metric, 3);

        if (readings.size() == 3) {
            if (isIncreasingTrend(readings, 10)) {
                triggerAlert(new Alert(patientId, increasingAlert, System.currentTimeMillis()));
            } else if (isDecreasingTrend(readings, 10)) {
                triggerAlert(new Alert(patientId, decreasingAlert, System.currentTimeMillis()));
            }
        }
    }

    private boolean isIncreasingTrend(List<Double> readings, double threshold) {
        for (int i = 1; i < readings.size(); i++) {
            if (readings.get(i) - readings.get(i - 1) <= threshold) {
                return false;
            }
        }
        return true;
    }

    private boolean isDecreasingTrend(List<Double> readings, double threshold) {
        for (int i = 1; i < readings.size(); i++) {
            if (readings.get(i - 1) - readings.get(i) <= threshold) {
                return false;
            }
        }
        return true;
    }

    public void triggerAlert(Alert alert) {
        System.out.println("Triggering alert: " + alert);
        AlertManager.getInstance().sendAlert(alert);
    }
}
