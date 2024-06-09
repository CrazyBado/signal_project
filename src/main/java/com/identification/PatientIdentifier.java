package com.identification;

import java.util.HashMap;
import java.util.Map;

/**
 * Handles patient identification and retrieval of patient records.
 */
public class PatientIdentifier {
    private Map<String, PatientRecord> patientDatabase;

    /**
     * Constructs a new PatientIdentifier.
     */
    public PatientIdentifier() {
        patientDatabase = new HashMap<>();
        // Assume patientDatabase is populated with patient records from the hospital database
    }

    /**
     * Matches the patient ID with existing records.
     *
     * @param patientId the patient ID to match
     * @return true if the patient ID exists, false otherwise
     */
    public boolean matchPatientId(String patientId) {
        return patientDatabase.containsKey(patientId);
    }

    /**
     * Returns the patient record for the given patient ID.
     *
     * @param patientId the patient ID
     * @return the patient record
     */
    public PatientRecord getPatientRecord(String patientId) {
        return patientDatabase.get(patientId);
    }

    /**
     * Adds a patient record to the database.
     *
     * @param record the patient record to add
     */
    public void addPatientRecord(PatientRecord record) {
        patientDatabase.put(record.getPatientId(), record);
    }
}
