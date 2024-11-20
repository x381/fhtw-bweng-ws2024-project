package at.fhtw.bweng_ws24.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER = "Bearer ";
    private final JwtVerifier jwtVerifier;
    private final JwtToPrincipalConverter jwtToPrincipalConverter;

    public JwtAuthenticationFilter(JwtVerifier jwtVerifier, JwtToPrincipalConverter jwtToPrincipalConverter) {
        this.jwtVerifier = jwtVerifier;
        this.jwtToPrincipalConverter = jwtToPrincipalConverter;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        extractBearerToken(request)
                .map(jwtVerifier::verify)
                .map(jwtToPrincipalConverter::convert)
                .map(UserPrincipalAuthenticationToken::new)
                .ifPresent(authentication -> {
                    SecurityContextHolder.getContext()
                            .setAuthentication(authentication);
                });

        filterChain.doFilter(request, response);
    }

    private Optional<String> extractBearerToken(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION);

        if (StringUtils.hasText(token) && token.startsWith(BEARER)) {
            return Optional.of(token.substring(BEARER.length()));
        }

        return Optional.empty();
    }

}

