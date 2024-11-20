package at.fhtw.bweng_ws24.security.jwt;

import at.fhtw.bweng_ws24.property.JwtProperties;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;


@Component
public class JwtIssuer implements TokenIssuer {

    private final JwtProperties properties;

    public JwtIssuer(JwtProperties properties) {
        this.properties = properties;
    }

    @Override
    public String issue(UUID userId, String username, String role) {
        return JWT.create()
                .withSubject(String.valueOf(userId))
                .withExpiresAt(Instant.now().plus(Duration.of(1, ChronoUnit.DAYS)))
                .withClaim("username", username)
                .withClaim("role", role)
                .sign(Algorithm.HMAC256(properties.getSecret()));
    }
}
