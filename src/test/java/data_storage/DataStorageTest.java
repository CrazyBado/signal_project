package data_storage;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.data_storage.DataStorage;
import com.data_storage.PatientData;
import com.data_storage.UnauthorizedAccessException;

public class DataStorageTest {

    private DataStorage dataStorage;

    @BeforeEach
    public void setUp() {
        dataStorage = new DataStorage();
        dataStorage.addUserRole("user1", "admin"); // Ensure user1 has the admin role
    }

    @Test
    public void testStoreData() throws UnauthorizedAccessException {
        Map<String, Double> metrics = new HashMap<>();
        metrics.put("heartRate", 72.0);
        PatientData patientData = new PatientData("patient123", metrics, System.currentTimeMillis());
        dataStorage.storeData(patientData);
        assertEquals(1, dataStorage.retrieveData("patient123", "user1").size());
    }

    @Test
    public void testRetrieveData() throws UnauthorizedAccessException {
        Map<String, Double> metrics = new HashMap<>();
        metrics.put("heartRate", 72.0);
        PatientData patientData = new PatientData("patient123", metrics, System.currentTimeMillis());
        dataStorage.storeData(patientData);

        assertEquals(1, dataStorage.retrieveData("patient123", "user1").size());
    }

    @Test
    public void testDeleteData() throws UnauthorizedAccessException {
        Map<String, Double> metrics = new HashMap<>();
        metrics.put("heartRate", 72.0);
        PatientData patientData = new PatientData("patient123", metrics, System.currentTimeMillis());
        dataStorage.storeData(patientData);
        dataStorage.deleteData("patient123", "user1");

        assertEquals(0, dataStorage.retrieveData("patient123", "user1").size());
    }

    @Test
    public void testIsAuthorized() {
        assertTrue(dataStorage.isAuthorized("user1", "patient123"));
    }
}

