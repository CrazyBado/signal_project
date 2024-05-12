package com.cardio_generator.generators;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * Interface for generating patient-specific health data.
 * Implementations of this interface should generate data based on the patient
 * ID provided and
 * utilize the given output strategy to handle the generated data appropriately.
 */

public interface PatientDataGenerator {
    /**
     * Generates data for a specific patient and sends it to an output strategy.
     *
     * @param patientId      the unique identifier for the patient
     * @param outputStrategy the strategy used to handle the output of the generated
     *                       data
     */

    void generate(int patientId, OutputStrategy outputStrategy);
}
