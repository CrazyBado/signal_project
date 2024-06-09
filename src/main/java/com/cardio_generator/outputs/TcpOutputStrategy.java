package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implements the OutputStrategy interface to send health data over TCP.
 * This strategy sets up a TCP server on a specified port and sends data to the
 * connected client.
 */
public class TcpOutputStrategy implements OutputStrategy {

    private static final Logger LOGGER = Logger.getLogger(TcpOutputStrategy.class.getName());

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;

    /**
     * Initializes a TCP server on a specified port and listens for client
     * connections.
     *
     * @param port the port number on which the server will listen
     */
    public TcpOutputStrategy(int port) {
        try {
            serverSocket = new ServerSocket(port);
            LOGGER.info("TCP Server started on port " + port);

            // Accept clients in a new thread to not block the main thread
            Executors.newSingleThreadExecutor().submit(() -> {
                try {
                    clientSocket = serverSocket.accept();
                    out = new PrintWriter(clientSocket.getOutputStream(), true);
                    LOGGER.info("Client connected: " + clientSocket.getInetAddress());
                } catch (IOException e) {
                    LOGGER.log(Level.SEVERE, "Error accepting client connection", e);
                }
            });

            // Add a shutdown hook to close the server when the application terminates
            Runtime.getRuntime().addShutdownHook(new Thread(this::stop));

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error starting TCP server on port " + port, e);
        }
    }

    /**
     * Sends formatted health data to the connected client over TCP.
     * Ensures data is only sent if a client is connected.
     *
     * @param patientId the identifier of the patient
     * @param timestamp the time at which the data is generated
     * @param label     a label describing the type of data
     * @param data      the actual data to be sent
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        if (out != null) {
            String message = String.format("%d,%d,%s,%s", patientId, timestamp, label, data);
            out.println(message);
        }
    }

    /**
     * Stops the TCP server and closes all resources.
     */
    public void stop() {
        try {
            if (out != null) {
                out.close();
            }
            if (clientSocket != null) {
                clientSocket.close();
            }
            if (serverSocket != null) {
                serverSocket.close();
            }
            LOGGER.info("TCP Server stopped");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error closing server resources", e);
        }
    }
}
