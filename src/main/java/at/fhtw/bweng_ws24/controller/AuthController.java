package at.fhtw.bweng_ws24.controller;

import at.fhtw.bweng_ws24.dto.TokenRequestDto;
import at.fhtw.bweng_ws24.dto.TokenResponseDto;
import at.fhtw.bweng_ws24.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/token")
    public TokenResponseDto token(@RequestBody @Valid final TokenRequestDto tokenRequestDto) {
        return authService.authenticate(tokenRequestDto);
    }
}
