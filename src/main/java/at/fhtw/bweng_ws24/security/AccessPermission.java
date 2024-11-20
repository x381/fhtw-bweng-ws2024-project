package at.fhtw.bweng_ws24.security;

import org.springframework.security.core.Authentication;

import java.util.UUID;

public interface AccessPermission {
    boolean supports(Authentication authentication, String className);
    boolean hasPermission(Authentication authentication, UUID resourceId);
}
