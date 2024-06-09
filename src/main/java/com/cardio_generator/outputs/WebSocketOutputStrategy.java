package com.cardio_generator.outputs;

import org.java_websocket.WebSocket;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implements the OutputStrategy interface to send health data over WebSocket.
 * This strategy sets up a WebSocket server on a specified port and sends data
 * to connected clients.
 */
public class WebSocketOutputStrategy implements OutputStrategy {

    private static final Logger LOGGER = Logger.getLogger(WebSocketOutputStrategy.class.getName());
    private WebSocketServer server;

    /**
     * Constructs a WebSocketOutputStrategy to create a WebSocket server on a
     * specified port.
     *
     * @param port the port number on which the server will listen
     */
    public WebSocketOutputStrategy(int port) {
        server = new SimpleWebSocketServer(new InetSocketAddress(port));
        LOGGER.info("WebSocket server created on port: " + port + ", listening for connections...");
        server.start();
    }

    /**
     * Outputs data to all connected WebSocket clients.
     *
     * @param patientId the identifier of the patient
     * @param timestamp the time at which the data is generated
     * @param label     a label describing the type of data
     * @param data      the actual data to be sent
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        String message = String.format("%d,%d,%s,%s", patientId, timestamp, label, data);
        // Broadcast the message to all connected clients
        for (WebSocket conn : server.getConnections()) {
            conn.send(message);
        }
    }

    /**
     * Simple WebSocket server to handle client connections and communication.
     */
    private static class SimpleWebSocketServer extends WebSocketServer {

        public SimpleWebSocketServer(InetSocketAddress address) {
            super(address);
        }

        @Override
        public void onOpen(WebSocket conn, org.java_websocket.handshake.ClientHandshake handshake) {
            LOGGER.info("New connection: " + conn.getRemoteSocketAddress());
        }

        @Override
        public void onClose(WebSocket conn, int code, String reason, boolean remote) {
            LOGGER.info("Closed connection: " + conn.getRemoteSocketAddress());
        }

        @Override
        public void onMessage(WebSocket conn, String message) {
            // Not used in this context
        }

        @Override
        public void onError(WebSocket conn, Exception ex) {
            LOGGER.log(Level.SEVERE, "WebSocket error", ex);
        }

        @Override
        public void onStart() {
            LOGGER.info("Server started successfully");
        }
    }
}
