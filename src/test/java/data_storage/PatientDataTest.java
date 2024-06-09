package data_storage;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.data_storage.PatientData;

public class PatientDataTest {

    private PatientData patientData;
    private Map<String, Double> metrics;

    @BeforeEach
    public void setUp() {
        metrics = new HashMap<>();
        metrics.put("heartRate", 72.0);
        patientData = new PatientData("patient123", metrics, System.currentTimeMillis());
    }

    @Test
    public void testGetPatientId() {
        assertEquals("patient123", patientData.getPatientId());
    }

    @Test
    public void testSetPatientId() {
        patientData.setPatientId("newPatientId");
        assertEquals("newPatientId", patientData.getPatientId());
    }

    @Test
    public void testGetMetrics() {
        assertEquals(metrics, patientData.getMetrics());
    }

    @Test
    public void testSetMetrics() {
        Map<String, Double> newMetrics = new HashMap<>();
        newMetrics.put("bloodPressure", 120.0);
        patientData.setMetrics(newMetrics);
        assertEquals(newMetrics, patientData.getMetrics());
    }

    @Test
    public void testGetTimestamp() {
        long timestamp = System.currentTimeMillis();
        patientData.setTimestamp(timestamp);
        assertEquals(timestamp, patientData.getTimestamp());
    }

    @Test
    public void testSetTimestamp() {
        long newTimestamp = System.currentTimeMillis();
        patientData.setTimestamp(newTimestamp);
        assertEquals(newTimestamp, patientData.getTimestamp());
    }
}
