package com.data_access;

import java.io.IOException;

import com.data_storage.DataStorage;
import com.data_storage.PatientData;

public class DataSourceAdapter {

    private DataStorage dataStorage;
    private DataParser dataParser;

    public DataSourceAdapter(DataStorage dataStorage, DataParser dataParser) {
        this.dataStorage = dataStorage;
        this.dataParser = dataParser;
    }

    public void processData(String rawData) {
        try {
            PatientData patientData = dataParser.parse(rawData);
            dataStorage.storeData(patientData);
            System.out.println("Data successfully processed and stored for patient ID: " + patientData.getPatientId());
        } catch (IOException e) {
            System.err.println("Error parsing data: " + e.getMessage());
            e.printStackTrace();
        }
    }
}