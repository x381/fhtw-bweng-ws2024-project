package at.fhtw.bweng_ws24.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.UUID;

import java.util.List;

@Data
public class OrderDto {

    @NotBlank(message = "Customer name is mandatory")
    @Size(min = 3, max = 50, message = "Customer name must be between 3 and 50 characters")
    private String customerName;

    @NotBlank(message = "Customer email is mandatory")
    @jakarta.validation.constraints.Email(message = "Invalid email format")
    private String customerEmail;

    @NotBlank(message = "Order address is mandatory")
    @Size(min = 5, max = 100, message = "Address must be between 5 and 100 characters")
    private String address;

    @PositiveOrZero(message = "Total amount must be zero or positive")
    private double totalAmount;

    @UUID(message = "Invalid UUID format")
    @NotBlank(message = "Created by is mandatory")
    private String createdBy;

    @NotBlank(message = "Order items cannot be empty")
    private List<String> orderItems; // List of product IDs or names
}