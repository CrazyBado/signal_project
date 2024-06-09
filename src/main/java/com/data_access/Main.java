package com.data_access;
import com.data_storage.DataStorage;

public class Main {
    public static void main(String[] args) {
        // Initialize the data storage system
        DataStorage dataStorage = new DataStorage();
        DataParser dataParser = new DataParser();
        DataSourceAdapter dataSourceAdapter = new DataSourceAdapter(dataStorage, dataParser);

        // Choose a data listener
        DataListener fileListener = new FileDataListener("data", dataSourceAdapter);
        //DataListener tcpListener = new TCPDataListener("localhost", 12345, dataSourceAdapter);
        //DataListener webSocketListener = new WebSocketDataListener("ws://localhost:8080", dataSourceAdapter);

        // Start listening for data
        fileListener.startListening();
        //tcpListener.startListening();
        //webSocketListener.startListening();

        // Stop listening (for example purposes, normally this would be controlled by the application logic)
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            fileListener.stopListening();
            //tcpListener.stopListening();
            //webSocketListener.stopListening();
        }));
    }
}
