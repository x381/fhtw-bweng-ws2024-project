package at.fhtw.bweng_ws24.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TokenRequestDto {

    @NotBlank
    private String usernameOrEmail;

    @NotBlank
    private String password;
    
}
