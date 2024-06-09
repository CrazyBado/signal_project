package com.cardio_generator.generators;

import java.util.Random;
import com.cardio_generator.outputs.OutputStrategy;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Generates alert data for patients based on a probability model.
 * This class maintains alert states for each patient and simulates the
 * triggering
 * and resolving of alerts based on random chance.
 */
public class AlertGenerator implements PatientDataGenerator {

    private static final Logger LOGGER = Logger.getLogger(AlertGenerator.class.getName());
    public static final Random randomGenerator = new Random();
    private static final double RESOLVE_PROBABILITY = 0.9;
    private static final double LAMBDA = 0.1; // Average rate (alerts per period), adjust based on desired frequency
    private static final double P = -Math.expm1(-LAMBDA); // Probability of at least one alert in the period
    private final boolean[] alertStates; // false = resolved, true = pressed

    /**
     * Initializes the AlertGenerator with a specified number of patients.
     * All patients start with their alerts state as resolved (false).
     * 
     * @param patientCount the number of patients to monitor for alerts
     */
    public AlertGenerator(int patientCount) {
        alertStates = new boolean[patientCount + 1];
    }

    /**
     * Simulates the generation of alerts for a specific patient.
     * Alerts can either be triggered or resolved based on defined probabilities.
     * 
     * @param patientId      the unique identifier for the patient
     * @param outputStrategy the strategy used to handle the output of the generated
     *                       alerts
     */
    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        try {
            if (alertStates[patientId]) {
                if (randomGenerator.nextDouble() < RESOLVE_PROBABILITY) { // 90% chance to resolve
                    alertStates[patientId] = false;
                    // Output the resolved alert
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "resolved");
                }
            } else {
                boolean alertTriggered = randomGenerator.nextDouble() < P;

                if (alertTriggered) {
                    alertStates[patientId] = true;
                    // Output the triggered alert
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "triggered");
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An error occurred while generating alert data for patient " + patientId, e);
        }
    }
}
