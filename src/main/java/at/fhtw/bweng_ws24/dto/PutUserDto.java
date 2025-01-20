package at.fhtw.bweng_ws24.dto;

import at.fhtw.bweng_ws24.model.UserGender;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.UUID;

@Data
public class PutUserDto {

    @NotBlank(message = "Username is mandatory")
    private String username;

    @Size(min = 3, max = 30, message = "First name must be between 3 and 30 characters")
    private String firstName;

    @Size(min = 3, max = 30, message = "Last name must be between 3 and 30 characters")
    private String lastName;

    private UserGender userGender;

    @Size(max = 30, message = "Other specify must be less than 30 characters")
    private String otherSpecify;

    @AssertTrue(message = "Other specify is mandatory if userGender is OTHER")
    public boolean isOtherSpecifyValid() {
        return (userGender == UserGender.OTHER && otherSpecify != null && !otherSpecify.isBlank())
                || (userGender != UserGender.OTHER && otherSpecify == null);
    }

    @Email(message = "Invalid email")
    @NotBlank(message = "Email is mandatory")
    private String email;

    @NotBlank(message = "Country is mandatory")
    @Size(min = 2, max = 30, message = "Country must be between 3 and 30 characters")
    private String country;

    @Size(min = 2, max = 30, message = "City must be between 2 and 30 characters")
    private String city;

    @Size(min = 2, max = 30, message = "Street must be between 2 and 30 characters")
    private String street;

    @Size(min = 2, max = 30, message = "Zip must be between 2 and 30 characters")
    private String zip;

    @Pattern(regexp = "^(\\+\\d{1,3}[- ]?)?\\d{10}$", message = "Invalid phone number")
    private String phone;

    @UUID(message = "Invalid UUID")
    @NotBlank(message = "Last updated by is mandatory")
    private String lastUpdatedBy;
}
