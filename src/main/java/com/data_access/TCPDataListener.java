package com.data_access;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TCPDataListener implements DataListener {

    private String host;
    private int port;
    private Socket clientSocket;
    private ExecutorService executor;
    private boolean isRunning;
    private DataSourceAdapter dataSourceAdapter;

    public TCPDataListener(String host, int port, DataSourceAdapter dataSourceAdapter) {
        this.host = host;
        this.port = port;
        this.dataSourceAdapter = dataSourceAdapter;
    }

    @Override
    public void startListening() {
        try {
            clientSocket = new Socket(host, port);
            System.out.println("Connected to TCP server at " + host + ":" + port);
            isRunning = true;
            executor = Executors.newSingleThreadExecutor();
            executor.submit(this::readData);
        } catch (IOException e) {
            System.err.println("Error connecting to TCP server at " + host + ":" + port);
            e.printStackTrace();
        }
    }

    @Override
    public void stopListening() {
        isRunning = false;
        if (executor != null) {
            executor.shutdown();
        }
        if (clientSocket != null) {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Error closing client socket");
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDataReceived(String rawData) {
        dataSourceAdapter.processData(rawData);
    }

    private void readData() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            String data;
            while (isRunning && (data = in.readLine()) != null) {
                onDataReceived(data);
            }
        } catch (IOException e) {
            System.err.println("Error reading from server");
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
                System.out.println("Connection to server closed");
            } catch (IOException e) {
                System.err.println("Error closing client socket");
                e.printStackTrace();
            }
        }
    }
}