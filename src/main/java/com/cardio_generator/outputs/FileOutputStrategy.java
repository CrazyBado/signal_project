package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implements the OutputStrategy interface for writing health data to files.
 * This strategy stores data in text files within a specified base directory.
 */

public class FileOutputStrategy implements OutputStrategy {

    private String BaseDirectory;

    /**
     * Maps each data label to its corresponding file path to ensure data is written
     * to the correct file.
     */

    public final ConcurrentHashMap<String, String> file_map = new ConcurrentHashMap<>();

    /**
     * Constructs a new FileOutputStrategy with a specified base directory.
     *
     * @param baseDirectory the directory where data files will be created and
     *                      managed
     */

    public FileOutputStrategy(String baseDirectory) {

        this.BaseDirectory = baseDirectory;
    }

    /**
     * Outputs data to the appropriate file based on the provided label.
     * Ensures that the directory and file exist before writing data.
     *
     * @param patientId the identifier of the patient
     * @param timestamp the timestamp of the data entry
     * @param label     the label describing the type of data
     * @param data      the actual data to be written
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        try {
            // Create the directory
            Files.createDirectories(Paths.get(BaseDirectory));
        } catch (IOException e) {
            System.err.println("Error creating base directory: " + e.getMessage());
            return;
        }
        // Set the FilePath variable
        String FilePath = file_map.computeIfAbsent(label, k -> Paths.get(BaseDirectory, label + ".txt").toString());

        // Write the data to the file
        try (PrintWriter out = new PrintWriter(
                Files.newBufferedWriter(Paths.get(FilePath), StandardOpenOption.CREATE, StandardOpenOption.APPEND))) {
            out.printf("Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n", patientId, timestamp, label, data);
        } catch (Exception e) {
            System.err.println("Error writing to file " + FilePath + ": " + e.getMessage());
        }
    }
}