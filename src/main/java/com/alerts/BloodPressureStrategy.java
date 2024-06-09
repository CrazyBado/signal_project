package com.alerts;

import java.util.Map;

public class BloodPressureStrategy implements AlertStrategy {

    @Override
    public void checkAlert(String patientId, Map<String, Double> metrics, AlertGenerator alertGenerator) {
        Double systolic = metrics.get("SystolicPressure");
        Double diastolic = metrics.get("DiastolicPressure");

        if (systolic == null || diastolic == null) {
            return;
        }

        if (systolic > 180 || systolic < 90 || diastolic > 120 || diastolic < 60) {
            alertGenerator.triggerAlert(new Alert(patientId, "Critical Blood Pressure", System.currentTimeMillis()));
        }

        // Add checks for trends if needed
    }
}
