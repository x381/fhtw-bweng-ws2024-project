package at.fhtw.bweng_ws24.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class ExceptionHandlingTest {

    private ExceptionHandling exceptionHandling;
    private WebRequest webRequest;

    @BeforeEach
    void setUp() {
        exceptionHandling = new ExceptionHandling();
        webRequest = mock(WebRequest.class);
    }

    @Test
    void testHandleNoSuchElementException() {
        // Arrange
        NoSuchElementException ex = new NoSuchElementException("Element not found");

        // Act
        ResponseEntity<Object> response = exceptionHandling.handleNoSuchElementException(ex, webRequest);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Element not found", response.getBody());
    }

    @Test
    void testHandleValidationExceptions() {
        // Arrange
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);

        // Simulating validation error details
        Map<String, String> errors = new HashMap<>();
        errors.put("field1", "must not be null");
        errors.put("field2", "must be a valid email");

        // Act
        ResponseEntity<Object> response = exceptionHandling.handleValidationExceptions(ex, webRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(errors, response.getBody());
    }
}