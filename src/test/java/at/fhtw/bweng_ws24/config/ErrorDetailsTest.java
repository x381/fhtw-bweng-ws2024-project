package at.fhtw.bweng_ws24.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ErrorDetailsTest {

    private ErrorDetails errorDetails;
    private Date timestamp;
    private String details;

    @BeforeEach
    public void setUp() {
        timestamp = new Date();
        details = "Error details";
        errorDetails = new ErrorDetails(timestamp, details);
    }

    @Test
    public void testErrorDetailsInitialization() {
        assertEquals(timestamp, errorDetails.getTimestamp());
        assertEquals(details, errorDetails.getDetails());
        assertTrue(errorDetails.getMessages().isEmpty(), "Messages map should be empty on initialization.");
    }

    @Test
    public void testAddMessage() {
        errorDetails.addMessage("fieldName", "Test message");
        assertEquals(1, errorDetails.getMessages().size());
        assertEquals("Test message", errorDetails.getMessages().get("fieldName"));
    }
}