package com.alerts;

import java.util.Map;

public interface AlertStrategy {

    void checkAlert(String patientId, Map<String, Double> metrics, AlertGenerator alertGenerator);
}
