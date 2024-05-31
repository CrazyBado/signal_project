package com.alerts;

import java.util.ArrayList;
import java.util.List;

public class AlertManager {

    private static AlertManager instance = new AlertManager();
    private List<AlertListener> listeners = new ArrayList<>();

    public static AlertManager getInstance() {
        return instance;
    }

    public void sendAlert(Alert alert) {
        for (AlertListener alertListener : listeners) {
            alertListener.onAlert(alert);
        }
    }

    public interface AlertListener {
        void onAlert(Alert alert);

    }
}
