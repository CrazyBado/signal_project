package com.cardio_generator.generators;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * Generates simulated ECG data for patients.
 * This class simulates ECG waveforms and outputs the data using a specified
 * output strategy.
 */
public class ECGDataGenerator implements PatientDataGenerator {

    private static final Logger LOGGER = Logger.getLogger(ECGDataGenerator.class.getName());
    private static final Random random = new Random();
    private static final double PI = Math.PI;

    private final double[] lastEcgValues;

    /**
     * Constructs an ECGDataGenerator for a specified number of patients.
     * Initializes the last ECG value for each patient.
     *
     * @param patientCount the number of patients to manage
     */
    public ECGDataGenerator(int patientCount) {
        lastEcgValues = new double[patientCount + 1];
        // Initialize the last ECG value for each patient
        for (int i = 1; i <= patientCount; i++) {
            lastEcgValues[i] = 0; // Initial ECG value can be set to 0
        }
    }

    /**
     * Generates and outputs new ECG data for a specified patient.
     * Simulates the ECG waveform based on patient data and system time.
     *
     * @param patientId      the ID of the patient
     * @param outputStrategy the strategy to output the generated data
     */
    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        try {
            double ecgValue = simulateEcgWaveform(patientId, lastEcgValues[patientId]);
            outputStrategy.output(patientId, System.currentTimeMillis(), "ECG", Double.toString(ecgValue));
            lastEcgValues[patientId] = ecgValue;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "An error occurred while generating ECG data for patient " + patientId, e);
        }
    }

    /**
     * Simulates an ECG waveform for a specified patient.
     * The simulation uses a combination of sinusoidal waves to represent different
     * components of the ECG.
     *
     * @param patientId    the ID of the patient
     * @param lastEcgValue the last ECG value generated for the patient
     * @return the simulated ECG value
     */
    private double simulateEcgWaveform(int patientId, double lastEcgValue) {
        // Simplified ECG waveform generation based on sinusoids
        double hr = 60.0 + random.nextDouble() * 20.0; // Simulate heart rate variability between 60 and 80 bpm
        double t = System.currentTimeMillis() / 1000.0; // Use system time to simulate continuous time
        double ecgFrequency = hr / 60.0; // Convert heart rate to Hz

        // Simulate different components of the ECG signal
        double pWave = 0.1 * Math.sin(2 * PI * ecgFrequency * t);
        double qrsComplex = 0.5 * Math.sin(2 * PI * 3 * ecgFrequency * t); // QRS is higher frequency
        double tWave = 0.2 * Math.sin(2 * PI * 2 * ecgFrequency * t + PI / 4); // T wave is offset

        return pWave + qrsComplex + tWave + random.nextDouble() * 0.05; // Add small noise
    }
}
