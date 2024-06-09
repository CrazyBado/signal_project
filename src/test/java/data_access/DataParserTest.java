package data_access;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.data_access.DataParser;
import com.data_storage.PatientData;

public class DataParserTest {

    private DataParser dataParser;

    @BeforeEach
    public void setUp() {
        dataParser = new DataParser();
    }

    @Test
    public void testParseValidData() throws IOException {
        String rawData = "{\"patientId\":\"patient1\",\"metrics\":{\"heartRate\":72.0,\"bloodPressure\":120.0},\"timestamp\":1627849200000}";

        PatientData patientData = dataParser.parse(rawData);

        assertNotNull(patientData);
        assertEquals("patient1", patientData.getPatientId());
        assertEquals(72.0, patientData.getMetrics().get("heartRate"));
        assertEquals(120.0, patientData.getMetrics().get("bloodPressure"));
        assertEquals(1627849200000L, patientData.getTimestamp());
    }

    @Test
    public void testParseInvalidData() {
        String rawData = "invalid data";

        assertThrows(IOException.class, () -> {
            dataParser.parse(rawData);
        });
    }

    @Test
    public void testParseMissingFields() {
        String rawData = "{\"patientId\":\"patient1\"}";

        assertThrows(NullPointerException.class, () -> {
            dataParser.parse(rawData);
        });
    }
}
