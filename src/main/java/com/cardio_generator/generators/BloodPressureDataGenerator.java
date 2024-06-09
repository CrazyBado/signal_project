package com.cardio_generator.generators;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * Generates blood pressure data for patients, including systolic and diastolic
 * pressure.
 * This class simulates data generation based on baseline values with small
 * variations.
 */
public class BloodPressureDataGenerator implements PatientDataGenerator {

    private static final Logger LOGGER = Logger.getLogger(BloodPressureDataGenerator.class.getName());
    private static final Random random = new Random();

    // Constants for baseline ranges and variations
    private static final int BASELINE_SYSTOLIC_MIN = 110;
    private static final int BASELINE_SYSTOLIC_RANGE = 20;
    private static final int BASELINE_DIASTOLIC_MIN = 70;
    private static final int BASELINE_DIASTOLIC_RANGE = 15;
    private static final int SYSTOLIC_VARIATION_RANGE = 5;
    private static final int DIASTOLIC_VARIATION_RANGE = 5;
    private static final int SYSTOLIC_MIN = 90;
    private static final int SYSTOLIC_MAX = 180;
    private static final int DIASTOLIC_MIN = 60;
    private static final int DIASTOLIC_MAX = 120;

    private final int[] lastSystolicValues;
    private final int[] lastDiastolicValues;

    /**
     * Initializes the BloodPressureDataGenerator with a specified number of
     * patients.
     * Generates baseline values for each patient.
     *
     * @param patientCount the number of patients to monitor for blood pressure
     */
    public BloodPressureDataGenerator(int patientCount) {
        lastSystolicValues = new int[patientCount + 1];
        lastDiastolicValues = new int[patientCount + 1];

        // Initialize with baseline values for each patient
        for (int i = 1; i <= patientCount; i++) {
            lastSystolicValues[i] = BASELINE_SYSTOLIC_MIN + random.nextInt(BASELINE_SYSTOLIC_RANGE);
            lastDiastolicValues[i] = BASELINE_DIASTOLIC_MIN + random.nextInt(BASELINE_DIASTOLIC_RANGE);
        }
    }

    /**
     * Generates blood pressure data for a specific patient and sends it to an
     * output strategy.
     *
     * @param patientId      the unique identifier for the patient
     * @param outputStrategy the strategy used to handle the output of the generated
     *                       data
     */
    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        try {
            int systolicVariation = random.nextInt(SYSTOLIC_VARIATION_RANGE) - 2; // -2, -1, 0, 1, or 2
            int diastolicVariation = random.nextInt(DIASTOLIC_VARIATION_RANGE) - 2;
            int newSystolicValue = lastSystolicValues[patientId] + systolicVariation;
            int newDiastolicValue = lastDiastolicValues[patientId] + diastolicVariation;

            // Ensure the blood pressure stays within a realistic and safe range
            newSystolicValue = Math.min(Math.max(newSystolicValue, SYSTOLIC_MIN), SYSTOLIC_MAX);
            newDiastolicValue = Math.min(Math.max(newDiastolicValue, DIASTOLIC_MIN), DIASTOLIC_MAX);

            lastSystolicValues[patientId] = newSystolicValue;
            lastDiastolicValues[patientId] = newDiastolicValue;

            outputStrategy.output(patientId, System.currentTimeMillis(), "SystolicPressure",
                    Integer.toString(newSystolicValue));
            outputStrategy.output(patientId, System.currentTimeMillis(), "DiastolicPressure",
                    Integer.toString(newDiastolicValue));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An error occurred while generating blood pressure data for patient " + patientId,
                    e);
        }
    }
}
