package at.fhtw.bweng_ws24.service;

import at.fhtw.bweng_ws24.dto.TokenRequestDto;
import at.fhtw.bweng_ws24.dto.TokenResponseDto;
import at.fhtw.bweng_ws24.model.User;
import at.fhtw.bweng_ws24.security.UserPrincipal;
import at.fhtw.bweng_ws24.security.jwt.TokenIssuer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private TokenIssuer tokenIssuer;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthService authService;

    @Test
    void authenticate_shouldIssueToken() {
        // Arrange
        TokenRequestDto tokenRequestDto = new TokenRequestDto();
        tokenRequestDto.setUsernameOrEmail("testUser");
        tokenRequestDto.setPassword("testPassword");

        User user = new User();
        user.setUsername("testUser");

        Authentication authentication = mock(Authentication.class);
        UUID userId = UUID.randomUUID();
        UserPrincipal userPrincipal = new UserPrincipal(userId, user.getUsername(), "testPassword", "ROLE_USER");

        when(userService.findByUsernameOrEmail(anyString(), anyString())).thenReturn(user);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userPrincipal);
        when(tokenIssuer.issue(any(UUID.class), anyString(), anyString())).thenReturn("generatedToken");

        // Act
        TokenResponseDto tokenResponseDto = authService.authenticate(tokenRequestDto);

        // Assert
        verify(userService).findByUsernameOrEmail("testUser", "testUser");
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenIssuer).issue(userPrincipal.getId(), userPrincipal.getUsername(), userPrincipal.getRole());

        assertEquals("generatedToken", tokenResponseDto.getToken());
    }

    @Test
    void authenticate_shouldThrowExceptionWhenUserNotFound() {
        // Arrange
        TokenRequestDto tokenRequestDto = new TokenRequestDto();
        tokenRequestDto.setUsernameOrEmail("nonExistentUser");
        tokenRequestDto.setPassword("testPassword");

        when(userService.findByUsernameOrEmail(anyString(), anyString())).thenReturn(null);

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> authService.authenticate(tokenRequestDto));

        verify(userService).findByUsernameOrEmail("nonExistentUser", "nonExistentUser");
        verifyNoInteractions(authenticationManager, tokenIssuer);
    }

    @Test
    void authenticate_shouldThrowExceptionWhenAuthenticationFails() {
        // Arrange
        TokenRequestDto tokenRequestDto = new TokenRequestDto();
        tokenRequestDto.setUsernameOrEmail("testUser");
        tokenRequestDto.setPassword("wrongPassword");

        User user = new User();
        user.setUsername("testUser");

        when(userService.findByUsernameOrEmail(anyString(), anyString())).thenReturn(user);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Authentication failed"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> authService.authenticate(tokenRequestDto));

        verify(userService).findByUsernameOrEmail("testUser", "testUser");
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verifyNoInteractions(tokenIssuer);
    }
}