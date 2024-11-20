package at.fhtw.bweng_ws24.security.jwt;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public interface TokenIssuer {

    String issue(UUID userId, String username, String role);
}
