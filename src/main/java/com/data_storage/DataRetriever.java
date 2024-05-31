package com.data_storage;

import java.util.List;

/**
 * Handles the retrieval of patient data from the DataStorage.
 */
public class DataRetriever {
    private DataStorage dataStorage;

    /**
     * Constructs a new DataRetriever object with the specified DataStorage.
     *
     * @param dataStorage the data storage object
     */
    public DataRetriever(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    /**
     * Fetches the current patient data if the user is authorized.
     *
     * @param patientId the patient ID
     * @param userId    the user ID
     * @return a list of patient data
     * @throws UnauthorizedAccessException if the user is not authorized to access
     *                                     the data
     */
    public List<PatientData> fetchPatientData(String patientId, String userId) throws UnauthorizedAccessException {
        return dataStorage.retrieveData(patientId, userId);
    }

    /**
     * Fetches the historical patient data if the user is authorized.
     *
     * @param patientId the patient ID
     * @param userId    the user ID
     * @return a list of historical patient data
     * @throws UnauthorizedAccessException if the user is not authorized to access
     *                                     the data
     */
    public List<PatientData> fetchHistoricalData(String patientId, String userId) throws UnauthorizedAccessException {
        return dataStorage.retrieveData(patientId, userId);
    }
}