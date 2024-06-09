package com.alerts;

import java.util.Map;

public class OxygenSaturationStrategy implements AlertStrategy {
    @Override
    public void checkAlert(String patientId, Map<String, Double> metrics, AlertGenerator alertGenerator) {
        Double saturation = metrics.get("Saturation");
        if (saturation == null) {
            return;
        }

        if (saturation < 92) {
            alertGenerator.triggerAlert(new Alert(patientId, "Low Blood Oxygen Saturation", System.currentTimeMillis()));
        }

        // Add checks for rapid drop if needed
    }
}
