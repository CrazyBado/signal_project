package com.data_access;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FileDataListener implements DataListener {

    private String directoryPath;
    private WatchService watchService;
    private ExecutorService executor;
    private boolean isRunning = false;
    private DataSourceAdapter dataSourceAdapter;

    public FileDataListener(String directoryPath, DataSourceAdapter dataSourceAdapter) {
        this.directoryPath = directoryPath;
        this.dataSourceAdapter = dataSourceAdapter;
    }

    @Override
    public void startListening() {
        try {
            this.watchService = FileSystems.getDefault().newWatchService();
            Path path = Paths.get(directoryPath);
            path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY);
            this.executor = Executors.newSingleThreadExecutor();
            this.isRunning = true;
            this.executor.submit(this::processEvents);
        } catch (Exception e) {
            System.err.println("Unable to start watch service for directory: " + directoryPath);
        }
    }

    @Override
    public void stopListening() {
        this.isRunning = false;
        if (executor != null) {
            executor.shutdown();
        }
        if (watchService != null) {
            try {
                watchService.close();
            } catch (Exception e) {
                System.err.println("Error closing watch service");
            }
        }
    }

    @Override
    public void onDataReceived(String rawData) {
        dataSourceAdapter.processData(rawData);
    }

    private void processEvents() {
        while (isRunning) {
            WatchKey key;
            try {
                key = watchService.take();
            } catch (Exception e) {
                System.out.println("Watch service interrupted");
                return;
            }
            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();

                WatchEvent<Path> ev = cast(event);
                Path fileName = ev.context();
                Path child = Paths.get(directoryPath).resolve(fileName);
                if (kind == StandardWatchEventKinds.OVERFLOW) {
                    continue;
                } else if (kind == StandardWatchEventKinds.ENTRY_CREATE || kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                    try {
                        String content = new String(Files.readAllBytes(child));
                        onDataReceived(content);
                    } catch (Exception e) {
                        System.err.println("Error reading files: " + child);
                    }
                }
            }

            boolean valid = key.reset();
            if (!valid) {
                break;
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> WatchEvent<T> cast(WatchEvent<?> event) {
        return (WatchEvent<T>) event;
    }
}