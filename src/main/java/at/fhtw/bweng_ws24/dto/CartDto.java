package at.fhtw.bweng_ws24.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.UUID;
import org.hibernate.validator.constraints.UniqueElements;


import java.util.List;

@Data
public class CartDto {
    @UUID(message = "Invalid UUID format")
    @NotBlank(message = "Created by is mandatory")
    private String createdBy;

    @Size(min = 1, message = "Cart must have at least one item")
    @UniqueElements(message = "Cart items must be unique")
    private List<CartItemDto> cartItems;
}
