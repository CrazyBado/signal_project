package com.data_storage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileDataReader implements DataReader {

    @Override
    public void readData(String outputDir, DataStorage dataStorage) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(outputDir));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] data = line.split(", ");
            String patientId = data[0].split(": ")[1];
            long timestamp = Long.parseLong(data[1].split(": ")[1]);
            String label = data[2].split(": ")[1];
            double value = Double.parseDouble(data[3].split(": ")[1]);
            Map<String, Double> metrics = new HashMap<>();
            metrics.put(label, value);
            PatientData patientData = new PatientData(patientId, metrics, timestamp);
            dataStorage.storeData(patientData);
        }
    }
}
