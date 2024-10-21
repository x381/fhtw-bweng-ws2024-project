package at.fhtw.bweng_ws24.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ProductDto {

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @Positive
    private double price;

}
