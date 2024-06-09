package com.data_access;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

public class WebSocketDataListener implements DataListener {

    private WebSocketClient client;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final String serverUri;
    private DataSourceAdapter dataSourceAdapter;

    public WebSocketDataListener(String serverUri, DataSourceAdapter dataSourceAdapter) {
        this.serverUri = serverUri;
        this.dataSourceAdapter = dataSourceAdapter;
        initializeWebSocketClient();
    }

    private void initializeWebSocketClient() {
        try {
            client = new WebSocketClient(new URI(serverUri), new Draft_6455()) {

                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    System.out.println("WebSocket connection opened to: " + getURI());
                }

                @Override
                public void onMessage(String message) {
                    System.out.println("Received message: " + message);
                    onDataReceived(message);
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    System.out.println("WebSocket connection closed: " + reason);
                }

                @Override
                public void onError(Exception ex) {
                    System.out.println("Error with WebSocket connection: " + ex.getMessage());
                    ex.printStackTrace();
                }
            };
        } catch (URISyntaxException e) {
            System.out.println("Invalid WebSocket URI: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void startListening() {
        if (client != null && !client.isOpen()) {
            executor.submit(() -> {
                client.connect();
                try {
                    while (!client.isOpen()) {
                        Thread.sleep(100);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("Error waiting for WebSocket connection to open: " + e.getMessage());
                    e.printStackTrace();
                }
            });
        }
    }

    @Override
    public void stopListening() {
        if (client != null && client.isOpen()) {
            executor.submit(() -> {
                client.close();
                executor.shutdown();
            });
        }
    }

    @Override
    public void onDataReceived(String rawData) {
        dataSourceAdapter.processData(rawData);
    }
}
