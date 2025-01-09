package at.fhtw.bweng_ws24.service;

import at.fhtw.bweng_ws24.dto.TokenRequestDto;
import at.fhtw.bweng_ws24.dto.TokenResponseDto;
import at.fhtw.bweng_ws24.model.User;
import at.fhtw.bweng_ws24.security.UserPrincipal;
import at.fhtw.bweng_ws24.security.jwt.TokenIssuer;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final TokenIssuer tokenIssuer;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    public AuthService(TokenIssuer tokenIssuer, AuthenticationManager authenticationManager, UserService userService) {
        this.tokenIssuer = tokenIssuer;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    public TokenResponseDto authenticate(TokenRequestDto tokenRequestDto) {
        User user = userService.findByUsernameOrEmail(tokenRequestDto.getUsernameOrEmail(), tokenRequestDto.getUsernameOrEmail());

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), tokenRequestDto.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        String token = tokenIssuer.issue(userPrincipal.getId(), userPrincipal.getUsername(), userPrincipal.getRole());

        return new TokenResponseDto(token);
    }
}
