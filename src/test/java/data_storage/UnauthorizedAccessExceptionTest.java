package data_storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import com.data_storage.UnauthorizedAccessException;

public class UnauthorizedAccessExceptionTest {

    @Test
    public void testExceptionMessage() {
        String message = "Access denied";
        UnauthorizedAccessException exception = new UnauthorizedAccessException(message);
        assertEquals(message, exception.getMessage());
    }
}
