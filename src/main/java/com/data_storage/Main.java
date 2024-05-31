package com.data_storage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Main class to demonstrate the usage of DataStorage and PatientData classes.
 */
public class Main {

    public static void main(String[] args) {
        // Example usage
        DataStorage storage = new DataStorage();

        // Creating sample patient data
        Map<String, Double> metrics = new HashMap<>();
        metrics.put("heartRate", 72.0);
        metrics.put("bloodPressure", 120.0);
        PatientData patientData = new PatientData("patient1", metrics, System.currentTimeMillis());

        // Adding user roles
        storage.addUserRole("doctor1", "doctor");
        storage.addUserRole("nurse1", "nurse");
        storage.addUserRole("patient1", "patient");

        // Storing data
        storage.storeData(patientData);

        try {
            // Retrieving data
            List<PatientData> retrievedData = storage.retrieveData("patient1", "doctor1");
            System.out.println("Retrieved Data: " + retrievedData.size());

            // Deleting data
            storage.deleteData("patient1", "doctor1");
            System.out.println("Data after deletion: " + storage.retrieveData("patient1", "doctor1").size());
        } catch (UnauthorizedAccessException e) {
            System.out.println(e.getMessage());
        }
    }
}