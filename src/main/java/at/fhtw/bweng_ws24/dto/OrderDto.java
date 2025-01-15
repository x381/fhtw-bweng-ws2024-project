package at.fhtw.bweng_ws24.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.UUID;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.List;

@Data
public class OrderDto {

    @NotBlank(message = "Customer name is mandatory")
    @Size(min = 3, max = 50, message = "Customer name must be between 3 and 50 characters")
    private String customerName;

    @NotBlank(message = "Customer email is mandatory")
    @Email(message = "Invalid email format")
    @Size(max = 50, message = "Email must be less than 50 characters")
    private String customerEmail;

    @NotBlank(message = "Order address is mandatory")
    @Size(min = 5, max = 100, message = "Address must be between 5 and 100 characters")
    private String address;

    @Positive(message = "Total amount must be positive")
    private double totalPrice;

    @UUID(message = "Invalid UUID format")
    @NotBlank(message = "Created by is mandatory")
    private String createdBy;

    @Size(min = 1, message = "Order must have at least one item")
    @UniqueElements(message = "Order items must be unique")
    private List<OrderItemDto> orderItems;
}