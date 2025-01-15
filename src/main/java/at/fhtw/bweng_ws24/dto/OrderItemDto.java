package at.fhtw.bweng_ws24.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class OrderItemDto {

    @NotBlank(message = "Product ID is mandatory")
    private String productId;

    @NotBlank(message = "Product name is mandatory")
    private String productName;

    @Positive(message = "Quantity must be positive")
    private int quantity;
}
