package com.data_access;

import com.data_storage.PatientData;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;

public class DataParser {

    private ObjectMapper objectMapper;

    public DataParser() {
        this.objectMapper = new ObjectMapper();
    }

    public PatientData parse(String rawData) throws IOException {
        // Assuming rawData is in JSON format
        @SuppressWarnings("unchecked")
        Map<String, Object> dataMap = objectMapper.readValue(rawData, Map.class);
        String patientId = (String) dataMap.get("patientId");
        @SuppressWarnings("unchecked")
        Map<String, Double> metrics = (Map<String, Double>) dataMap.get("metrics");
        long timestamp = (long) dataMap.get("timestamp");

        return new PatientData(patientId, metrics, timestamp);
    }
}