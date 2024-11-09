package at.fhtw.bweng_ws24.config;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;

@Data
public class ErrorDetails {
    private Date timestamp;
    private ArrayList<String> messages;
    private String details;

    public ErrorDetails(Date timestamp, String details) {
        this.timestamp = timestamp;
        this.messages = new ArrayList<>();
        this.details = details;
    }

    public void addMessage(String message) {
        this.messages.add(message);
    }
}
