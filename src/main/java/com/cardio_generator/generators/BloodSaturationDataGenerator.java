package com.cardio_generator.generators;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * Generates simulated blood saturation data for patients.
 * This class simulates fluctuations in blood saturation levels within a healthy
 * range and outputs the data using a specified output strategy.
 */
public class BloodSaturationDataGenerator implements PatientDataGenerator {

    private static final Logger LOGGER = Logger.getLogger(BloodSaturationDataGenerator.class.getName());
    private static final Random random = new Random();

    // Constants for baseline ranges and variations
    private static final int BASELINE_MIN = 95;
    private static final int BASELINE_RANGE = 6;
    private static final int VARIATION_RANGE = 3;
    private static final int SATURATION_MIN = 90;
    private static final int SATURATION_MAX = 100;

    private final int[] lastSaturationValues;

    /**
     * Constructs a BloodSaturationDataGenerator for a specified number of patients.
     * Initializes baseline saturation values for each patient between 95% and 100%.
     *
     * @param patientCount the number of patients to manage
     */
    public BloodSaturationDataGenerator(int patientCount) {
        lastSaturationValues = new int[patientCount + 1];

        // Initialize with baseline saturation values for each patient
        for (int i = 1; i <= patientCount; i++) {
            lastSaturationValues[i] = BASELINE_MIN + random.nextInt(BASELINE_RANGE); // Initializes with a value between
                                                                                     // 95 and 100
        }
    }

    /**
     * Generates and outputs new saturation data for a specified patient.
     * Ensures that saturation levels remain within a realistic range of 90% to
     * 100%.
     *
     * @param patientId      the ID of the patient
     * @param outputStrategy the strategy to output the generated data
     */
    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        try {
            // Simulate blood saturation values
            int variation = random.nextInt(VARIATION_RANGE) - 1; // -1, 0, or 1 to simulate small fluctuations
            int newSaturationValue = lastSaturationValues[patientId] + variation;

            // Ensure the saturation stays within a realistic and healthy range
            newSaturationValue = Math.min(Math.max(newSaturationValue, SATURATION_MIN), SATURATION_MAX);
            lastSaturationValues[patientId] = newSaturationValue;

            outputStrategy.output(patientId, System.currentTimeMillis(), "Saturation", newSaturationValue + "%");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE,
                    "An error occurred while generating blood saturation data for patient " + patientId, e);
        }
    }
}
