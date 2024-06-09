package com.alerts;

import java.util.Map;

public class HeartRateStrategy implements AlertStrategy {
    @Override
    public void checkAlert(String patientId, Map<String, Double> metrics, AlertGenerator alertGenerator) {
        Double heartRate = metrics.get("HeartRate");
        if (heartRate == null) {
            return;
        }
    }
}
