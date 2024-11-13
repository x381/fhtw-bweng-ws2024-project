package at.fhtw.bweng_ws24.config;

import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
public class ErrorDetails {
    private Date timestamp;
    private Map<String, Object> messages;
    private String details;

    public ErrorDetails(Date timestamp, String details) {
        this.timestamp = timestamp;
        this.messages = new HashMap<>();
        this.details = details;
    }

    public void addMessage(String fieldName, Object content) {
        this.messages.put(fieldName, content);
    }
}
