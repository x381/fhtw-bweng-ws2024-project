package at.fhtw.bweng_ws24.config;

import at.fhtw.bweng_ws24.exception.EmailExistsException;
import at.fhtw.bweng_ws24.exception.PasswordWrongException;
import at.fhtw.bweng_ws24.exception.StockNotEnoughException;
import at.fhtw.bweng_ws24.exception.UsernameExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@ControllerAdvice
public class ExceptionHandling {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> handleNoSuchElementException(NoSuchElementException e, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), request.getDescription(false));
        errorDetails.addMessage("errors", e.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(
            MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        ErrorDetails errorDetails = new ErrorDetails(new Date(), request.getDescription(false));
        errorDetails.addMessage("errors", errors);
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException e, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), request.getDescription(false));
        errorDetails.addMessage("errors", e.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({EmailExistsException.class, UsernameExistsException.class, PasswordWrongException.class, StockNotEnoughException.class})
    public ResponseEntity<?> handleCustomExceptions(Exception e, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), request.getDescription(false));
        errorDetails.addMessage("errors", e.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
}
