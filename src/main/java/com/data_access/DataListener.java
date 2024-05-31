package com.data_access;

/**
 * Interface for listening to data from various sources.
 */
public interface DataListener {
    /**
     * Starts listening for data.
     */
    void startListening();

    /**
     * Stops listening for data.
     */
    void stopListening();

    /**
     * Processes raw data received from the source.
     *
     * @param rawData the raw data received
     */
    void onDataReceived(String rawData);
}