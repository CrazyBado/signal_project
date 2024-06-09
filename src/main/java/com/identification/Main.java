package com.identification;

import java.util.Arrays;
import java.util.Date;

/**
 * Main class to demonstrate the usage of PatientIdentifier and PatientRecord
 * classes.
 */
public class Main {
    public static void main(String[] args) {
        // Example usage
        PatientIdentifier patientIdentifier = new PatientIdentifier();

        // Adding sample patient records to the identifier
        PatientRecord record1 = new PatientRecord("patient1", "John Doe", new Date(),
                Arrays.asList("Allergy: Penicillin", "Asthma"));
        PatientRecord record2 = new PatientRecord("patient2", "Jane Smith", new Date(),
                Arrays.asList("Diabetes", "Hypertension"));

        patientIdentifier.addPatientRecord(record1);
        patientIdentifier.addPatientRecord(record2);

        IdentityManager identityManager = new IdentityManager(patientIdentifier);

        try {
            // Linking data to a patient
            PatientRecord linkedRecord = identityManager.linkDataToPatient("patient1");
            System.out.println("Linked Record: " + linkedRecord.getName());

            // Handling discrepancies
            identityManager.handleDiscrepancies("patient1", "patient1_corrected");
            PatientRecord updatedRecord = patientIdentifier.getPatientRecord("patient1_corrected");
            System.out.println("Updated Record: " + updatedRecord.getName());
        } catch (PatientNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
