package at.fhtw.bweng_ws24.dto;

import at.fhtw.bweng_ws24.model.UserGender;
import lombok.Data;

import java.util.UUID;

@Data
public class UserResponseDto {
    private UUID id;
    private String username;
    private String firstName;
    private String lastName;
    private UserGender userGender;
    private String otherSpecify;
    private String email;
    private String country;
    private String city;
    private String street;
    private String zip;
    private String phone;
    private String image;
    private String role;
}
