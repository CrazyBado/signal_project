package data_access;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.data_access.DataParser;
import com.data_access.DataSourceAdapter;
import com.data_access.FileDataListener;
import com.data_storage.DataStorage;
import com.data_storage.UnauthorizedAccessException;

public class FileDataListenerTest {

    private static final String TEST_DIRECTORY = "test_data";
    private FileDataListener fileDataListener;
    private DataSourceAdapter dataSourceAdapter;
    private DataStorage dataStorage;
    private DataParser dataParser;

    @BeforeEach
    public void setUp() throws IOException {
        dataStorage = new DataStorage();
        dataParser = new DataParser();
        dataSourceAdapter = new DataSourceAdapter(dataStorage, dataParser);
        fileDataListener = new FileDataListener(TEST_DIRECTORY, dataSourceAdapter);

        // Create test directory if it does not exist
        Path testDir = Paths.get(TEST_DIRECTORY);
        if (!Files.exists(testDir)) {
            Files.createDirectories(testDir);
        }
    }

    @AfterEach
    public void tearDown() throws IOException {
        fileDataListener.stopListening();

        // Delete test directory and its content
        Path testDir = Paths.get(TEST_DIRECTORY);
        if (Files.exists(testDir)) {
            Files.walk(testDir)
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
    }

    @Test
    public void testStartListening() throws IOException, InterruptedException, UnauthorizedAccessException {
        fileDataListener.startListening();

        // Create a test file in the directory
        File testFile = new File(TEST_DIRECTORY, "test.txt");
        try (FileWriter writer = new FileWriter(testFile)) {
            writer.write("{\"patientId\":\"patient1\",\"metrics\":{\"heartRate\":72.0,\"bloodPressure\":120.0},\"timestamp\":1627849200000}");
        }

        // Add a delay to let the listener process the file
        Thread.sleep(1000);

        fileDataListener.stopListening();

        assertEquals(2, dataStorage.retrieveData("patient1", "user1").size());
        testFile.delete();
    }

    @Test
    public void testStopListening() {
        fileDataListener.startListening();
        fileDataListener.stopListening();
        // Ensure no exceptions are thrown and listener stops correctly
        assertTrue(true);
    }

    @Test
    public void testOnDataReceived() throws UnauthorizedAccessException {
        String rawData = "{\"patientId\":\"patient1\",\"metrics\":{\"heartRate\":72.0,\"bloodPressure\":120.0},\"timestamp\":1627849200000}";
        fileDataListener.onDataReceived(rawData);
        assertEquals(1, dataStorage.retrieveData("patient1", "user1").size());
    }
}
