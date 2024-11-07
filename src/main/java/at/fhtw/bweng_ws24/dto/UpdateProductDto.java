package at.fhtw.bweng_ws24.dto;

import at.fhtw.bweng_ws24.model.ProductCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.hibernate.validator.constraints.UUID;

@Data
public class UpdateProductDto {

    @NotBlank(message = "Name is mandatory")
    private String name;

    // TODO
//    @NotBlank
//    private String imageUrl;

    private ProductCategory category;

    @NotBlank(message = "Description is mandatory")
    private String description;

    @UUID(message = "Invalid UUID")
    private String lastUpdatedBy;

    @Positive(message = "Stock must be positive")
    private int stock;

    @Positive(message = "Price must be positive")
    private double price;
}
