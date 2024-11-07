package at.fhtw.bweng_ws24.dto;

import at.fhtw.bweng_ws24.model.UserGender;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateUserDto {

    @NotBlank(message = "Username is mandatory")
    private String username;

    private UserGender userGender;

    private String otherSpecify;

    @AssertTrue(message = "Other specify is mandatory only if userGender is OTHER")
    public boolean isOtherSpecifyValid() {
        return (userGender == UserGender.OTHER && otherSpecify != null && !otherSpecify.isBlank())
                || (userGender != UserGender.OTHER && otherSpecify == null);
    }

    @Email(message = "Invalid email")
    private String email;

    @NotBlank(message = "Password is mandatory")
    private String password;

    @NotBlank(message = "country is mandatory")
    private String country;
}
