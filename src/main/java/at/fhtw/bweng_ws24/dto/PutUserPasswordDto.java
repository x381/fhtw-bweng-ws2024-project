package at.fhtw.bweng_ws24.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.UUID;

@Data
public class PutUserPasswordDto {

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!.])(?=\\S+$).{12,}$", message = "Actual Password must contain at least one digit, one lowercase letter, one uppercase letter, one symbol, no whitespace, and at least 12 characters")
    @NotBlank(message = "Actual password is mandatory")
    private String actualPassword;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!.])(?=\\S+$).{12,}$", message = "New Password must contain at least one digit, one lowercase letter, one uppercase letter, one symbol, no whitespace, and at least 12 characters")
    @NotBlank(message = "New password is mandatory")
    private String newPassword;

    @UUID(message = "Invalid UUID")
    @NotBlank(message = "Last updated by is mandatory")
    private String lastUpdatedBy;
}
