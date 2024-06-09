package data_storage;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.data_storage.DataRetriever;
import com.data_storage.DataStorage;
import com.data_storage.PatientData;
import com.data_storage.UnauthorizedAccessException;

public class DataRetrieverTest {

    private DataRetriever dataRetriever;
    private DataStorage dataStorage;

    @BeforeEach
    public void setUp() {
        dataStorage = new DataStorage();
        dataStorage.addUserRole("user1", "admin");
        dataRetriever = new DataRetriever(dataStorage);
    }

    @Test
    public void testFetchPatientData() throws UnauthorizedAccessException {
        dataStorage.addUserRole("user1", "admin");
        Map<String, Double> metrics = new HashMap<>();
        metrics.put("heartRate", 72.0);
        PatientData patientData = new PatientData("patient123", metrics, System.currentTimeMillis());
        dataStorage.storeData(patientData);

        assertEquals(1, dataRetriever.fetchPatientData("patient123", "user1").size());
    }

    @Test
    public void testFetchHistoricalData() throws UnauthorizedAccessException {
        dataStorage.addUserRole("user1", "admin");
        Map<String, Double> metrics = new HashMap<>();
        metrics.put("heartRate", 72.0);
        PatientData patientData = new PatientData("patient123", metrics, System.currentTimeMillis());
        dataStorage.storeData(patientData);

        assertEquals(1, dataRetriever.fetchHistoricalData("patient123", "user1").size());
    }
}
