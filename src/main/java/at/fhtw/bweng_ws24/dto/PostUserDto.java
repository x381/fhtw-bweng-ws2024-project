package at.fhtw.bweng_ws24.dto;

import at.fhtw.bweng_ws24.model.UserGender;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class PostUserDto {

    @NotBlank(message = "Username is mandatory")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    private String username;

    private UserGender userGender;

    @Size(  max = 30, message = "Other specify must be less than 30 characters")
    private String otherSpecify;

    @AssertTrue(message = "Other specify is mandatory only if userGender is OTHER")
    public boolean isOtherSpecifyValid() {
        return (userGender == UserGender.OTHER && otherSpecify != null && !otherSpecify.isBlank())
                || (userGender != UserGender.OTHER && otherSpecify == null);
    }

    @Email(message = "Invalid email")
    @NotBlank(message = "Email is mandatory")
    @Size(max = 50, message = "Email must be less than 50 characters")
    private String email;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!.])(?=\\S+$).{12,}$", message = "Password must contain at least one digit, one lowercase letter, one uppercase letter, one symbol, no whitespace, and at least 12 characters")
    @NotBlank(message = "Password is mandatory")
    private String password;

    @NotBlank(message = "Country is mandatory")
    @Size(min = 2, max = 30, message = "Country must be between 3 and 30 characters")
    private String country;
}
