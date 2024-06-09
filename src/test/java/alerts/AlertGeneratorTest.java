package alerts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.alerts.Alert;
import com.alerts.AlertGenerator;
import com.alerts.AlertManager;
import com.cardio_generator.HealthDataSimulator;
import com.data_storage.DataStorage;
import com.data_storage.PatientData;

public class AlertGeneratorTest {

    private DataStorage dataStorage;
    private AlertGenerator alertGenerator;
    private AlertManager alertManager;
    private TestAlertListener testAlertListener;
    private HealthDataSimulator healthDataSimulator;

    @BeforeEach
    public void setUp() {
        dataStorage = new DataStorage();
        alertGenerator = new AlertGenerator(dataStorage);
        alertManager = AlertManager.getInstance();
        testAlertListener = new TestAlertListener();
        alertManager.addListener(testAlertListener);
        healthDataSimulator = new HealthDataSimulator(dataStorage);
    }

    @Test
    public void testEvaluateDataForCriticalBloodPressure() {
        Map<String, Double> metrics = new HashMap<>();
        metrics.put("SystolicPressure", 185.0);
        metrics.put("DiastolicPressure", 125.0);
        PatientData patientData = new PatientData("1", metrics, System.currentTimeMillis());
        dataStorage.storeData(patientData);

        alertGenerator.evaluateData(patientData);

        // Check if alert is triggered for critical blood pressure
        assertTrue(testAlertListener.isAlertTriggered("1", "Critical Blood Pressure"));
    }

    @Test
    public void testEvaluateDataForLowBloodOxygenSaturation() {
        Map<String, Double> metrics = new HashMap<>();
        metrics.put("Saturation", 88.0);
        PatientData patientData = new PatientData("1", metrics, System.currentTimeMillis());
        dataStorage.storeData(patientData);

        alertGenerator.evaluateData(patientData);

        // Check if alert is triggered for low blood oxygen saturation
        assertTrue(testAlertListener.isAlertTriggered("1", "Low Blood Oxygen Saturation"));
    }

    @Test
    public void testEvaluateDataForHypotensiveHypoxemia() {
        Map<String, Double> metrics = new HashMap<>();
        metrics.put("SystolicPressure", 85.0);
        metrics.put("Saturation", 88.0);
        PatientData patientData = new PatientData("1", metrics, System.currentTimeMillis());
        dataStorage.storeData(patientData);

        alertGenerator.evaluateData(patientData);

        // Check if alert is triggered for Hypotensive Hypoxemia
        assertTrue(testAlertListener.isAlertTriggered("1", "Hypotensive Hypoxemia"));
    }

    @Test
    public void testEvaluateDataForAbnormalECG() {
        Map<String, Double> metrics = new HashMap<>();
        metrics.put("ECG", 120.0);
        PatientData patientData = new PatientData("1", metrics, System.currentTimeMillis());
        dataStorage.storeData(patientData);

        // Adding previous ECG readings
        for (int i = 0; i < 5; i++) {
            Map<String, Double> previousMetrics = new HashMap<>();
            previousMetrics.put("ECG", 80.0);
            dataStorage.storeData(new PatientData("1", previousMetrics, System.currentTimeMillis() - (i + 1) * 1000));
        }

        alertGenerator.evaluateData(patientData);

        // Check if alert is triggered for abnormal ECG data
        assertFalse(testAlertListener.isAlertTriggered("1", "Abnormal ECG Data"));
    }

    @Test
    public void testEvaluateDataForBloodPressureIncreasingTrend() {
        Map<String, Double> metrics1 = new HashMap<>();
        metrics1.put("SystolicPressure", 110.0);
        Map<String, Double> metrics2 = new HashMap<>();
        metrics2.put("SystolicPressure", 121.0);
        Map<String, Double> metrics3 = new HashMap<>();
        metrics3.put("SystolicPressure", 132.0);

        dataStorage.storeData(new PatientData("1", metrics1, System.currentTimeMillis() - 2000));
        dataStorage.storeData(new PatientData("1", metrics2, System.currentTimeMillis() - 1000));
        dataStorage.storeData(new PatientData("1", metrics3, System.currentTimeMillis()));

        alertGenerator.evaluateData(new PatientData("1", metrics3, System.currentTimeMillis()));

        // Check if alert is triggered for increasing blood pressure trend
        assertFalse(testAlertListener.isAlertTriggered("1", "Blood Pressure Increasing Trend"));
    }

    @Test
    public void testEvaluateDataForBloodPressureDecreasingTrend() {
        Map<String, Double> metrics1 = new HashMap<>();
        metrics1.put("SystolicPressure", 150.0);
        Map<String, Double> metrics2 = new HashMap<>();
        metrics2.put("SystolicPressure", 138.0);
        Map<String, Double> metrics3 = new HashMap<>();
        metrics3.put("SystolicPressure", 126.0);

        dataStorage.storeData(new PatientData("1", metrics1, System.currentTimeMillis() - 2000));
        dataStorage.storeData(new PatientData("1", metrics2, System.currentTimeMillis() - 1000));
        dataStorage.storeData(new PatientData("1", metrics3, System.currentTimeMillis()));

        alertGenerator.evaluateData(new PatientData("1", metrics3, System.currentTimeMillis()));

        // Check if alert is triggered for decreasing blood pressure trend
        assertFalse(testAlertListener.isAlertTriggered("1", "Blood Pressure Decreasing Trend"));
    }

    @Test
    public void testEvaluateDataForRapidDropInBloodOxygenSaturation() {
        Map<String, Double> metrics1 = new HashMap<>();
        metrics1.put("Saturation", 97.0);
        Map<String, Double> metrics2 = new HashMap<>();
        metrics2.put("Saturation", 91.0);

        dataStorage.storeData(new PatientData("1", metrics1, System.currentTimeMillis() - 600000));
        dataStorage.storeData(new PatientData("1", metrics2, System.currentTimeMillis()));

        alertGenerator.evaluateData(new PatientData("1", metrics2, System.currentTimeMillis()));

        // Check if alert is triggered for rapid drop in blood oxygen saturation
        assertFalse(testAlertListener.isAlertTriggered("1", "Rapid Drop in Blood Oxygen Saturation"));
    }

    @Test
    public void testManualAlertTrigger() {
        HealthDataSimulator.simulateManualAlert("1", "Manual Alert Condition");

        // Check if manual alert is triggered
        assertTrue(testAlertListener.isAlertTriggered("1", "Manual Alert Condition"));
    }

    // Custom AlertListener for testing purposes
    private static class TestAlertListener implements AlertManager.AlertListener {

        private List<Alert> triggeredAlerts = new ArrayList<>();

        @Override
        public void onAlert(Alert alert) {
            triggeredAlerts.add(alert);
        }

        public boolean isAlertTriggered(String patientId, String alertType) {
            System.out.println("Checking if alert is triggered for patient " + patientId + " and type " + alertType);
            return triggeredAlerts.stream()
                    .anyMatch(alert -> alert.getPatientId().equals(patientId) && alert.getCondition().equals(alertType));
        }
    }
}
