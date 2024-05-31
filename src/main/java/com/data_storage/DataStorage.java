package com.data_storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages the storage and retrieval of patient data.
 */
public class DataStorage {
    private Map<String, List<PatientData>> dataStorage;
    private Map<String, String> userRoles;

    /**
     * Constructs a new DataStorage object.
     */
    public DataStorage() {
        dataStorage = new HashMap<>();
        userRoles = new HashMap<>();
    }

    /**
     * Stores patient data.
     *
     * @param data the patient data to store
     */
    public void storeData(PatientData data) {
        String patientId = data.getPatientId();
        dataStorage.computeIfAbsent(patientId, k -> new ArrayList<>()).add(data);
    }

    /**
     * Retrieves patient data if the user is authorized.
     *
     * @param patientId the patient ID
     * @param userId    the user ID
     * @return a list of patient data
     * @throws UnauthorizedAccessException if the user is not authorized to access
     *                                     the data
     */
    public List<PatientData> retrieveData(String patientId, String userId) throws UnauthorizedAccessException {
        if (isAuthorized(userId, patientId)) {
            return dataStorage.getOrDefault(patientId, new ArrayList<>());
        } else {
            throw new UnauthorizedAccessException("User not authorized to access this data");
        }
    }

    /**
     * Deletes patient data if the user is authorized.
     *
     * @param patientId the patient ID
     * @param userId    the user ID
     * @throws UnauthorizedAccessException if the user is not authorized to delete
     *                                     the data
     */
    public void deleteData(String patientId, String userId) throws UnauthorizedAccessException {
        if (isAuthorized(userId, patientId)) {
            dataStorage.remove(patientId);
        } else {
            throw new UnauthorizedAccessException("User not authorized to delete this data");
        }
    }

    /**
     * Adds a role for a user.
     *
     * @param userId the user ID
     * @param role   the role of the user
     */
    public void addUserRole(String userId, String role) {
        userRoles.put(userId, role);
    }

    /**
     * Checks if a user is authorized to access or modify data.
     *
     * @param userId    the user ID
     * @param patientId the patient ID
     * @return true if the user is authorized, false otherwise
     */
    private boolean isAuthorized(String userId, String patientId) {
        String role = userRoles.get(userId);
        if (role == null)
            return false;

        // Example role-based access control
        if (role.equals("doctor") || role.equals("nurse")) {
            return true;
        } else if (role.equals("patient") && userId.equals(patientId)) {
            return true;
        } else {
            return false;
        }
    }
}