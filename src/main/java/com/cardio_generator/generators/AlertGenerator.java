package com.cardio_generator.generators;

import java.util.Random;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * Generates alert data for patients based on a probability model.
 * This class maintains alert states for each patient and simulates the triggering
 * and resolving of alerts based on random chance.
 */

public class AlertGenerator implements PatientDataGenerator {

    public static final Random randomGenerator = new Random();
    private boolean[] AlertStates; // false = resolved, true = pressed

    /**
     * 
     * Initializes the AlertGenerator with a specified number of patients.
     * All patients start with their alerts state as resolved (false)
     * 
     * @param patientCount the number of patients to monitor for alerts
     */

    public AlertGenerator(int patientCount) {
        AlertStates = new boolean[patientCount + 1];
    }

    /**
     * 
     * Simulates the generation of alerts for a specific patient.
     * Alerts can either be triggered of resolved based on defined probalities
     * 
     * @param patientId      the unique idemtifier for the patient
     * @param outputStrategy the strategy used to handle the output of the generated
     */

    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        try {
            if (AlertStates[patientId]) {
                if (randomGenerator.nextDouble() < 0.9) { // 90% chance to resolve
                    AlertStates[patientId] = false;
                    // Output the alert
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "resolved");
                }
            } else {
                double Lambda = 0.1; // Average rate (alerts per period), adjust based on desired frequency
                double p = -Math.expm1(-Lambda); // Probability of at least one alert in the period
                boolean alertTriggered = randomGenerator.nextDouble() < p;

                if (alertTriggered) {
                    AlertStates[patientId] = true;
                    // Output the alert
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "triggered");
                }
            }
        } catch (Exception e) {
            System.err.println("An error occurred while generating alert data for patient " + patientId);
            e.printStackTrace();
        }
    }
}