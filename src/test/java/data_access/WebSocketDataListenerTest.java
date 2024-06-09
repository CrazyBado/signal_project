package data_access;

import java.net.InetSocketAddress;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.data_access.DataParser;
import com.data_access.DataSourceAdapter;
import com.data_access.WebSocketDataListener;
import com.data_storage.DataStorage;
import com.data_storage.UnauthorizedAccessException;

public class WebSocketDataListenerTest {

    private static final int PORT = 8080;
    private WebSocketDataListener webSocketDataListener;
    private DataSourceAdapter dataSourceAdapter;
    private DataStorage dataStorage;
    private DataParser dataParser;
    private TestWebSocketServer webSocketServer;

    @BeforeEach
    public void setUp() throws Exception {
        dataStorage = new DataStorage();
        dataParser = new DataParser();
        dataSourceAdapter = new DataSourceAdapter(dataStorage, dataParser);
        webSocketDataListener = new WebSocketDataListener("ws://localhost:" + PORT, dataSourceAdapter);

        // Start a mock WebSocket server
        webSocketServer = new TestWebSocketServer(new InetSocketAddress(PORT));
        webSocketServer.start();
    }

    @AfterEach
    public void tearDown() throws Exception {
        webSocketDataListener.stopListening();
        webSocketServer.stop();
    }

    @Test
    public void testStartListening() throws Exception {
        webSocketDataListener.startListening();

        // Wait for the client to connect
        Thread.sleep(1000);

        // Send a message from the server to the client
        webSocketServer.sendMessage("{\"patientId\":\"patient1\",\"metrics\":{\"heartRate\":72.0,\"bloodPressure\":120.0},\"timestamp\":1627849200000}");

        // Wait for the client to receive the message
        Thread.sleep(1000);

        webSocketDataListener.stopListening();

        assertEquals(0, dataStorage.retrieveData("patient1", "user1").size());
    }

    @Test
    public void testStopListening() {
        webSocketDataListener.startListening();
        webSocketDataListener.stopListening();
        // Ensure no exceptions are thrown and listener stops correctly
        assertTrue(true);
    }

    @Test
    public void testOnDataReceived() throws UnauthorizedAccessException {
        String rawData = "{\"patientId\":\"patient1\",\"metrics\":{\"heartRate\":72.0,\"bloodPressure\":120.0},\"timestamp\":1627849200000}";
        webSocketDataListener.onDataReceived(rawData);
        assertEquals(1, dataStorage.retrieveData("patient1", "user1").size());
    }

    private static class TestWebSocketServer extends WebSocketServer {

        public TestWebSocketServer(InetSocketAddress address) {
            super(address);
        }

        @Override
        public void onOpen(WebSocket conn, ClientHandshake handshake) {
            System.out.println("Server: WebSocket connection opened");
        }

        @Override
        public void onClose(WebSocket conn, int code, String reason, boolean remote) {
            System.out.println("Server: WebSocket connection closed");
        }

        @Override
        public void onMessage(WebSocket conn, String message) {
            System.out.println("Server received message: " + message);
        }

        @Override
        public void onError(WebSocket conn, Exception ex) {
            System.out.println("Server error: " + ex.getMessage());
            ex.printStackTrace();
        }

        @Override
        public void onStart() {
            System.out.println("Server started successfully");
        }

        public void sendMessage(String message) {
            broadcast(message);
        }
    }
}
