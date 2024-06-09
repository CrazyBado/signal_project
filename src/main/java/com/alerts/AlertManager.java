package com.alerts;

import java.util.ArrayList;
import java.util.List;

public class AlertManager {

    private static volatile AlertManager instance;
    private final List<AlertListener> listeners;

    private AlertManager() {
        listeners = new ArrayList<>();
    }

    public static AlertManager getInstance() {
        if (instance == null) {
            synchronized (AlertManager.class) {
                if (instance == null) {
                    instance = new AlertManager();
                }
            }
        }
        return instance;
    }

    public void sendAlert(Alert alert) {
        synchronized (listeners) {
            for (AlertListener alertListener : listeners) {
                alertListener.onAlert(alert);
            }
        }
    }

    public void addListener(AlertListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }

    public void removeListener(AlertListener listener) {
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }

    public interface AlertListener {
        void onAlert(Alert alert);
    }
}
