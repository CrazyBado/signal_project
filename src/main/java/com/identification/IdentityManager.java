package com.identification;

/**
 * Manages linking data to patient records and resolving discrepancies.
 */
public class IdentityManager {
    private PatientIdentifier patientIdentifier;

    /**
     * Constructs a new IdentityManager.
     *
     * @param patientIdentifier the patient identifier
     */
    public IdentityManager(PatientIdentifier patientIdentifier) {
        this.patientIdentifier = patientIdentifier;
    }

    /**
     * Links incoming data to the correct patient record.
     *
     * @param patientId the patient ID
     * @return the patient record
     * @throws PatientNotFoundException if the patient ID is not found
     */
    public PatientRecord linkDataToPatient(String patientId) throws PatientNotFoundException {
        if (patientIdentifier.matchPatientId(patientId)) {
            return patientIdentifier.getPatientRecord(patientId);
        } else {
            throw new PatientNotFoundException("Patient ID not found: " + patientId);
        }
    }

    /**
     * Resolves discrepancies in patient ID matching.
     *
     * @param patientId        the incorrect patient ID
     * @param correctPatientId the correct patient ID
     */
    public void handleDiscrepancies(String patientId, String correctPatientId) {
        PatientRecord record = patientIdentifier.getPatientRecord(patientId);
        if (record != null) {
            patientIdentifier.addPatientRecord(new PatientRecord(correctPatientId, record.getName(),
                    record.getDateOfBirth(), record.getMedicalHistory()));
            // Optionally, remove the old record with the incorrect patient ID
        }
    }
}
