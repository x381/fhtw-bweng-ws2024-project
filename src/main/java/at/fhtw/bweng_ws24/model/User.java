package at.fhtw.bweng_ws24.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String username;

    private String firstName;

    private String lastName;

    @Enumerated(EnumType.STRING)
    private UserGender userGender;

    private String otherSpecify;

    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole userRole = UserRole.USER;

    private String country;

    private String city;

    private String street;

    private String zip;

    private String phone;

    private String image;

    private boolean enabled = true;

    private UUID lastUpdatedBy;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant lastUpdatedAt;
}
