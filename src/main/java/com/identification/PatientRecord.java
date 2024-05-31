package com.identification;

import java.util.Date;
import java.util.List;

/**
 * Represents a record of a patient, including their ID, name, date of birth,
 * and medical history.
 */
public class PatientRecord {
    private String patientId;
    private String name;
    private Date dateOfBirth;
    private List<String> medicalHistory;

    /**
     * Constructs a new PatientRecord.
     *
     * @param patientId      the unique identifier for the patient
     * @param name           the name of the patient
     * @param dateOfBirth    the date of birth of the patient
     * @param medicalHistory the medical history of the patient
     */
    public PatientRecord(String patientId, String name, Date dateOfBirth, List<String> medicalHistory) {
        this.patientId = patientId;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.medicalHistory = medicalHistory;
    }

    /**
     * Returns the patient ID.
     *
     * @return the patient ID
     */
    public String getPatientId() {
        return patientId;
    }

    /**
     * Sets the patient ID.
     *
     * @param patientId the patient ID
     */
    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    /**
     * Returns the name of the patient.
     *
     * @return the name of the patient
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the patient.
     *
     * @param name the name of the patient
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the date of birth of the patient.
     *
     * @return the date of birth of the patient
     */
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Sets the date of birth of the patient.
     *
     * @param dateOfBirth the date of birth of the patient
     */
    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * Returns the medical history of the patient.
     *
     * @return the medical history of the patient
     */
    public List<String> getMedicalHistory() {
        return medicalHistory;
    }

    /**
     * Sets the medical history of the patient.
     *
     * @param medicalHistory the medical history of the patient
     */
    public void setMedicalHistory(List<String> medicalHistory) {
        this.medicalHistory = medicalHistory;
    }
}