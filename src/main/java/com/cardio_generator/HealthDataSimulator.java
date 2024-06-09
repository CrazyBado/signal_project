package com.cardio_generator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.alerts.Alert;
import com.alerts.AlertGenerator;
import com.cardio_generator.generators.BloodLevelsDataGenerator;
import com.cardio_generator.generators.BloodPressureDataGenerator;
import com.cardio_generator.generators.BloodSaturationDataGenerator;
import com.cardio_generator.generators.ECGDataGenerator;
import com.cardio_generator.outputs.ConsoleOutputStrategy;
import com.cardio_generator.outputs.FileOutputStrategy;
import com.cardio_generator.outputs.OutputStrategy;
import com.cardio_generator.outputs.TcpOutputStrategy;
import com.cardio_generator.outputs.WebSocketOutputStrategy;
import com.data_storage.DataStorage;

/**
 * Simulates health data generation for multiple patients using various data
 * generators. This simulator is capable of using different output strategies to
 * handle the generated data. It supports console, file, WebSocket, and TCP
 * output based on command line arguments.
 *
 * <p>
 * Usage: java HealthDataSimulator [options]
 * <p>
 * Options:
 * <ul>
 * <li>-h: Show help and exit.
 * <li>--patient-count <count>: Specify the number of patients to simulate data
 * for (default: 50).
 * <li>--output <type>: Define the output method. Options include 'console',
 * 'file:<directory>', 'websocket:<port>', 'tcp:<port>'.
 * </ul>
 * </p>
 */
public class HealthDataSimulator {

    private static final Logger LOGGER = Logger.getLogger(HealthDataSimulator.class.getName());
    private static int patientCount = 50; // Default number of patients
    private static ScheduledExecutorService scheduler;
    private static OutputStrategy outputStrategy = new ConsoleOutputStrategy(); // Default output strategy
    private static final Random random = new Random();
    private static AlertGenerator alertGenerator; // Make AlertGenerator static for easy access

    public HealthDataSimulator(DataStorage dataStorage) {
        alertGenerator = new AlertGenerator(dataStorage);
    }

    /**
     * Main entry point for the Health Data Simulator. Parses command line
     * arguments and sets up the simulation.
     *
     * @param args command line arguments used to customize the simulation
     * settings
     * @throws IOException if an I/O error occurs when setting up output
     * directories
     */
    public static void main(String[] args) throws IOException {
        parseArguments(args);

        scheduler = Executors.newScheduledThreadPool(patientCount * 4);

        List<Integer> patientIds = initializePatientIds(patientCount);
        Collections.shuffle(patientIds); // Randomize the order of patient IDs

        scheduleTasksForPatients(patientIds);

        // Add a shutdown hook to gracefully stop the scheduler
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (scheduler != null && !scheduler.isShutdown()) {
                scheduler.shutdown();
                try {
                    if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                        scheduler.shutdownNow();
                    }
                } catch (InterruptedException e) {
                    scheduler.shutdownNow();
                }
            }
        }));
    }

    /**
     * Parses command line arguments to configure the simulator.
     *
     * @param args array of command line arguments
     * @throws IOException if an I/O error occurs, particularly when creating
     * directories
     */
    private static void parseArguments(String[] args) throws IOException {
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-h":
                    printHelp();
                    System.exit(0);
                    break;
                case "--patient-count":
                    if (i + 1 < args.length) {
                        try {
                            patientCount = Integer.parseInt(args[++i]);
                        } catch (NumberFormatException e) {
                            LOGGER.log(Level.WARNING, "Invalid number of patients. Using default value: {0}",
                                    patientCount);
                        }
                    }
                    break;
                case "--output":
                    if (i + 1 < args.length) {
                        String outputArg = args[++i];
                        if (outputArg.equals("console")) {
                            outputStrategy = new ConsoleOutputStrategy();
                        } else if (outputArg.startsWith("file:")) {
                            String baseDirectory = outputArg.substring(5);
                            Path outputPath = Paths.get(baseDirectory);
                            if (!Files.exists(outputPath)) {
                                Files.createDirectories(outputPath);
                            }
                            outputStrategy = new FileOutputStrategy(baseDirectory);
                        } else if (outputArg.startsWith("websocket:")) {
                            try {
                                int port = Integer.parseInt(outputArg.substring(10));
                                outputStrategy = new WebSocketOutputStrategy(port);
                                LOGGER.info("WebSocket output will be on port: " + port);
                            } catch (NumberFormatException e) {
                                LOGGER.log(Level.SEVERE,
                                        "Invalid port for WebSocket output. Please specify a valid port number.");
                            }
                        } else if (outputArg.startsWith("tcp:")) {
                            try {
                                int port = Integer.parseInt(outputArg.substring(4));
                                outputStrategy = new TcpOutputStrategy(port);
                                LOGGER.info("TCP socket output will be on port: " + port);
                            } catch (NumberFormatException e) {
                                LOGGER.log(Level.SEVERE,
                                        "Invalid port for TCP output. Please specify a valid port number.");
                            }
                        } else {
                            LOGGER.warning("Unknown output type. Using default (console).");
                        }
                    }
                    break;
                default:
                    LOGGER.warning("Unknown option '" + args[i] + "'");
                    printHelp();
                    System.exit(1);
            }
        }
    }

    /**
     * Prints usage information for the HealthDataSimulator.
     */
    private static void printHelp() {
        System.out.println("Usage: java HealthDataSimulator [options]");
        System.out.println("Options:");
        System.out.println("  -h                       Show help and exit.");
        System.out.println(
                "  --patient-count <count>  Specify the number of patients to simulate data for (default: 50).");
        System.out.println("  --output <type>          Define the output method. Options are:");
        System.out.println("                             'console' for console output,");
        System.out.println("                             'file:<directory>' for file output,");
        System.out.println("                             'websocket:<port>' for WebSocket output,");
        System.out.println("                             'tcp:<port>' for TCP socket output.");
        System.out.println("Example:");
        System.out.println("  java HealthDataSimulator --patient-count 100 --output websocket:8080");
        System.out.println(
                "  This command simulates data for 100 patients and sends the output to WebSocket clients connected to port 8080.");
    }

    /**
     * Initializes a list of patient IDs.
     *
     * @param patientCount the number of patients to generate IDs for
     * @return a list of integer IDs
     */
    private static List<Integer> initializePatientIds(int patientCount) {
        List<Integer> patientIds = new ArrayList<>();
        for (int i = 1; i <= patientCount; i++) {
            patientIds.add(i);
        }
        return patientIds;
    }

    /**
     * Schedules tasks for each patient to generate health data periodically.
     *
     * @param patientIds a shuffled list of patient IDs
     */
    private static void scheduleTasksForPatients(List<Integer> patientIds) {
        ECGDataGenerator ecgDataGenerator = new ECGDataGenerator(patientCount);
        BloodSaturationDataGenerator bloodSaturationDataGenerator = new BloodSaturationDataGenerator(patientCount);
        BloodPressureDataGenerator bloodPressureDataGenerator = new BloodPressureDataGenerator(patientCount);
        BloodLevelsDataGenerator bloodLevelsDataGenerator = new BloodLevelsDataGenerator(patientCount);
        //alertGenerator = new AlertGenerator(new DataStorage());

        for (int patientId : patientIds) {
            scheduleTask(() -> ecgDataGenerator.generate(patientId, outputStrategy), 1, TimeUnit.SECONDS);
            scheduleTask(() -> bloodSaturationDataGenerator.generate(patientId, outputStrategy), 1, TimeUnit.SECONDS);
            scheduleTask(() -> bloodPressureDataGenerator.generate(patientId, outputStrategy), 1, TimeUnit.MINUTES);
            scheduleTask(() -> bloodLevelsDataGenerator.generate(patientId, outputStrategy), 2, TimeUnit.MINUTES);
            //scheduleTask(() -> alertGenerator.generate(patientId, outputStrategy), 20, TimeUnit.SECONDS);
        }
    }

    /**
     * Schedules a repeating task with a fixed delay.
     *
     * @param task the task to schedule
     * @param period the period between successive executions
     * @param timeUnit the time unit of the period
     */
    private static void scheduleTask(Runnable task, long period, TimeUnit timeUnit) {
        scheduler.scheduleAtFixedRate(task, random.nextInt(5), period, timeUnit);
    }

    /**
     * Simulates a manual alert trigger for testing purposes.
     *
     * @param patientId The ID of the patient.
     * @param condition The condition triggering the alert.
     */
    public static void simulateManualAlert(String patientId, String condition) {
        Alert alert = new Alert(patientId, condition, System.currentTimeMillis());
        alertGenerator.triggerAlert(alert); // Directly use AlertGenerator to trigger an alert
    }
}
