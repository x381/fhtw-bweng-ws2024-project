package at.fhtw.bweng_ws24.dto;

import at.fhtw.bweng_ws24.model.ProductCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.UUID;

@Data
public class PutProductDto {

    @NotBlank(message = "Name is mandatory")
    @Size(min = 3, max = 30, message = "Name must be between 3 and 30 characters")
    private String name;

    // TODO
//    @NotBlank
//    private String imageUrl;

    private ProductCategory category;

    @NotBlank(message = "Description is mandatory")
    @Size(min = 3, max = 100, message = "Description must be between 3 and 100 characters")
    private String description;

    @UUID(message = "Invalid UUID")
    @NotBlank(message = "Last updated by is mandatory")
    private String lastUpdatedBy;

    @PositiveOrZero(message = "Stock must be positive or zero")
    private int stock;

    @Positive(message = "Price must be positive")
    private double price;
}