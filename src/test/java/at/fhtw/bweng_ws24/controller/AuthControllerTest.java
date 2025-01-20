package at.fhtw.bweng_ws24.controller;

import at.fhtw.bweng_ws24.dto.TokenRequestDto;
import at.fhtw.bweng_ws24.dto.TokenResponseDto;
import at.fhtw.bweng_ws24.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuthControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testTokenEndpoint_Success() throws Exception {
        TokenRequestDto requestDto = new TokenRequestDto();
        requestDto.setUsernameOrEmail("user@example.com");
        requestDto.setPassword("password123");

        TokenResponseDto responseDto = new TokenResponseDto("validToken");

        when(authService.authenticate(any(TokenRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/auth/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.token").value("validToken"));
    }

    @Test
    void testTokenEndpoint_InvalidRequest() throws Exception {
        TokenRequestDto requestDto = new TokenRequestDto();
        requestDto.setUsernameOrEmail("");
        requestDto.setPassword("");

        mockMvc.perform(post("/auth/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }
}
