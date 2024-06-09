package com.cardio_generator.generators;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * Generates blood level data for patients, including cholesterol, white blood
 * cells,
 * and red blood cells. This class simulates data generation based on baseline
 * values
 * with small variations.
 */
public class BloodLevelsDataGenerator implements PatientDataGenerator {

    private static final Logger LOGGER = Logger.getLogger(BloodLevelsDataGenerator.class.getName());
    private static final Random random = new Random();

    // Constants for baseline ranges
    private static final double BASELINE_CHOLESTEROL_MIN = 150;
    private static final double BASELINE_CHOLESTEROL_RANGE = 50;
    private static final double BASELINE_WHITE_CELLS_MIN = 4;
    private static final double BASELINE_WHITE_CELLS_RANGE = 6;
    private static final double BASELINE_RED_CELLS_MIN = 4.5;
    private static final double BASELINE_RED_CELLS_RANGE = 1.5;

    private final double[] baselineCholesterol;
    private final double[] baselineWhiteCells;
    private final double[] baselineRedCells;

    /**
     * Initializes the BloodLevelsDataGenerator with a specified number of patients.
     * Generates baseline values for each patient.
     *
     * @param patientCount the number of patients to monitor for blood levels
     */
    public BloodLevelsDataGenerator(int patientCount) {
        // Initialize arrays to store baseline values for each patient
        baselineCholesterol = new double[patientCount + 1];
        baselineWhiteCells = new double[patientCount + 1];
        baselineRedCells = new double[patientCount + 1];

        // Generate baseline values for each patient
        for (int i = 1; i <= patientCount; i++) {
            baselineCholesterol[i] = BASELINE_CHOLESTEROL_MIN + random.nextDouble() * BASELINE_CHOLESTEROL_RANGE;
            baselineWhiteCells[i] = BASELINE_WHITE_CELLS_MIN + random.nextDouble() * BASELINE_WHITE_CELLS_RANGE;
            baselineRedCells[i] = BASELINE_RED_CELLS_MIN + random.nextDouble() * BASELINE_RED_CELLS_RANGE;
        }
    }

    /**
     * Generates blood level data for a specific patient and sends it to an output
     * strategy.
     *
     * @param patientId      the unique identifier for the patient
     * @param outputStrategy the strategy used to handle the output of the generated
     *                       data
     */
    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        try {
            // Generate values around the baseline for realism
            double cholesterol = baselineCholesterol[patientId] + (random.nextDouble() - 0.5) * 10; // Small variation
            double whiteCells = baselineWhiteCells[patientId] + (random.nextDouble() - 0.5) * 1; // Small variation
            double redCells = baselineRedCells[patientId] + (random.nextDouble() - 0.5) * 0.2; // Small variation

            // Output the generated values
            outputStrategy.output(patientId, System.currentTimeMillis(), "Cholesterol", Double.toString(cholesterol));
            outputStrategy.output(patientId, System.currentTimeMillis(), "WhiteBloodCells",
                    Double.toString(whiteCells));
            outputStrategy.output(patientId, System.currentTimeMillis(), "RedBloodCells", Double.toString(redCells));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An error occurred while generating blood levels data for patient " + patientId,
                    e);
        }
    }
}
