package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implements the OutputStrategy interface for writing health data to files.
 * This strategy stores data in text files within a specified base directory.
 */
public class FileOutputStrategy implements OutputStrategy {

    private static final Logger LOGGER = Logger.getLogger(FileOutputStrategy.class.getName());
    private static final String FILE_EXTENSION = ".txt";

    private final String baseDirectory;
    private final ConcurrentHashMap<String, String> fileMap = new ConcurrentHashMap<>();

    /**
     * Constructs a new FileOutputStrategy with a specified base directory.
     *
     * @param baseDirectory the directory where data files will be created and
     *                      managed
     */
    public FileOutputStrategy(String baseDirectory) {
        this.baseDirectory = baseDirectory;
    }

    /**
     * Outputs data to the appropriate file based on the provided label.
     * Ensures that the directory and file exist before writing data.
     *
     * @param patientId the identifier of the patient
     * @param timestamp the timestamp of the data entry, in milliseconds since the
     *                  epoch
     * @param label     the label describing the type of data (e.g., "HeartRate",
     *                  "BloodPressure")
     * @param data      the actual data to be written
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        try {
            // Create the base directory if it does not exist
            Files.createDirectories(Paths.get(baseDirectory));
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error creating base directory: " + baseDirectory, e);
            return;
        }

        // Set the file path
        String filePath = fileMap.computeIfAbsent(label,
                k -> Paths.get(baseDirectory, label + FILE_EXTENSION).toString());

        // Write the data to the file
        try (PrintWriter out = new PrintWriter(Files.newBufferedWriter(Paths.get(filePath),
                StandardOpenOption.CREATE, StandardOpenOption.APPEND))) {
            out.printf("Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n", patientId, timestamp, label, data);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error writing to file " + filePath, e);
        }
    }
}
