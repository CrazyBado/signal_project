package data_access;

import java.io.IOException;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.data_access.DataParser;
import com.data_access.DataSourceAdapter;
import com.data_storage.DataStorage;
import com.data_storage.PatientData;

public class DataSourceAdapterTest {

    private DataStorage dataStorage;
    private DataParser dataParser;
    private DataSourceAdapter dataSourceAdapter;

    @BeforeEach
    public void setUp() {
        dataStorage = mock(DataStorage.class);
        dataParser = mock(DataParser.class);
        dataSourceAdapter = new DataSourceAdapter(dataStorage, dataParser);
    }

    @Test
    public void testProcessDataValid() throws IOException {
        String rawData = "patient1,72.0,120.0";
        PatientData patientData = new PatientData("patient1", Map.of("heartRate", 72.0, "bloodPressure", 120.0), System.currentTimeMillis());

        when(dataParser.parse(rawData)).thenReturn(patientData);

        dataSourceAdapter.processData(rawData);

        verify(dataParser).parse(rawData);
        verify(dataStorage).storeData(patientData);
    }

    @Test
    public void testProcessDataInvalid() throws IOException {
        String rawData = "invalid data";
        IOException parseException = new IOException("Invalid data format");

        when(dataParser.parse(rawData)).thenThrow(parseException);

        dataSourceAdapter.processData(rawData);

        verify(dataParser).parse(rawData);
        verify(dataStorage, never()).storeData(any(PatientData.class));
    }
}
