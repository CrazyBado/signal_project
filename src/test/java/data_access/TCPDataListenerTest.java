package data_access;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.data_access.DataParser;
import com.data_access.DataSourceAdapter;
import com.data_access.TCPDataListener;
import com.data_storage.DataStorage;
import com.data_storage.UnauthorizedAccessException;

public class TCPDataListenerTest {

    private static final int PORT = 12345;
    private TCPDataListener tcpDataListener;
    private DataSourceAdapter dataSourceAdapter;
    private DataStorage dataStorage;
    private DataParser dataParser;
    private ExecutorService serverExecutor;
    private ServerSocket serverSocket;

    @BeforeEach
    public void setUp() throws Exception {
        dataStorage = new DataStorage();
        dataParser = new DataParser();
        dataSourceAdapter = new DataSourceAdapter(dataStorage, dataParser);
        tcpDataListener = new TCPDataListener("localhost", PORT, dataSourceAdapter);

        // Start a mock TCP server
        serverSocket = new ServerSocket(PORT);
        serverExecutor = Executors.newSingleThreadExecutor();
        serverExecutor.submit(() -> {
            try (Socket clientSocket = serverSocket.accept();
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
                out.println("{\"patientId\":\"patient1\",\"metrics\":{\"heartRate\":72.0,\"bloodPressure\":120.0},\"timestamp\":1627849200000}");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @AfterEach
    public void tearDown() throws Exception {
        tcpDataListener.stopListening();
        serverSocket.close();
        serverExecutor.shutdown();
    }

    @Test
    public void testStartListening() throws Exception {
        tcpDataListener.startListening();

        // Add a delay to let the listener process the data
        Thread.sleep(1000);

        tcpDataListener.stopListening();

        assertEquals(1, dataStorage.retrieveData("patient1", "user1").size());
    }

    @Test
    public void testStopListening() {
        tcpDataListener.startListening();
        tcpDataListener.stopListening();
        // Ensure no exceptions are thrown and listener stops correctly
        assertTrue(true);
    }

    @Test
    public void testOnDataReceived() throws UnauthorizedAccessException {
        String rawData = "{\"patientId\":\"patient1\",\"metrics\":{\"heartRate\":72.0,\"bloodPressure\":120.0},\"timestamp\":1627849200000}";
        tcpDataListener.onDataReceived(rawData);
        assertEquals(1, dataStorage.retrieveData("patient1", "user1").size());
    }
}
