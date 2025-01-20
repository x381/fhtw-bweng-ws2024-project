package at.fhtw.bweng_ws24.controller;

import at.fhtw.bweng_ws24.dto.PostUserDto;
import at.fhtw.bweng_ws24.dto.PutUserDto;
import at.fhtw.bweng_ws24.dto.PutUserPasswordDto;
import at.fhtw.bweng_ws24.dto.UserResponseDto;
import at.fhtw.bweng_ws24.model.UserGender;
import at.fhtw.bweng_ws24.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testGetUsers() throws Exception {
        List<UserResponseDto> users = Arrays.asList(
                createUserResponseDto(UUID.randomUUID(), "user1"),
                createUserResponseDto(UUID.randomUUID(), "user2")
        );
        when(userService.getUsers()).thenReturn(users);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].username").value("user1"))
                .andExpect(jsonPath("$[1].username").value("user2"));
    }

    @Test
    void testGetUser() throws Exception {
        UUID userId = UUID.randomUUID();
        UserResponseDto user = createUserResponseDto(userId, "testUser");
        when(userService.getUserResponseDto(userId)).thenReturn(user);

        mockMvc.perform(get("/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(userId.toString()))
                .andExpect(jsonPath("$.username").value("testUser"));
    }

    @Test
    void testCreateUser() throws Exception {
        PostUserDto postUserDto = createPostUserDto();
        UUID userId = UUID.randomUUID();
        when(userService.createUser(any(PostUserDto.class))).thenReturn(userId);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postUserDto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/users/" + userId))
                .andExpect(jsonPath("$.message").value("User created successfully"))
                .andExpect(jsonPath("$.userId").value(userId.toString()));
    }

    @Test
    void testUpdateUser() throws Exception {
        UUID userId = UUID.randomUUID();
        PutUserDto putUserDto = createPutUserDto();

        mockMvc.perform(put("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(putUserDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User updated successfully"));

        verify(userService).updateUser(eq(userId), any(PutUserDto.class));
    }

    @Test
    void testDeleteUser() throws Exception {
        UUID userId = UUID.randomUUID();

        mockMvc.perform(delete("/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User deleted successfully"));

        verify(userService).deleteUser(userId);
    }

    @Test
    void testUpdateUserPassword() throws Exception {
        UUID userId = UUID.randomUUID();
        PutUserPasswordDto passwordDto = new PutUserPasswordDto();
        passwordDto.setActualPassword("oldPassword123!");
        passwordDto.setNewPassword("newPassword123!");
        passwordDto.setLastUpdatedBy(UUID.randomUUID().toString());

        mockMvc.perform(put("/users/{id}/password", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(passwordDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User password updated successfully"));

        verify(userService).updateUserPassword(eq(userId), any(PutUserPasswordDto.class));
    }

    private UserResponseDto createUserResponseDto(UUID id, String username) {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(id);
        dto.setUsername(username);
        dto.setEmail(username + "@example.com");
        dto.setUserGender(UserGender.MALE);
        dto.setCountry("USA");
        dto.setCreatedAt(Instant.now());
        return dto;
    }

    private PostUserDto createPostUserDto() {
        PostUserDto dto = new PostUserDto();
        dto.setUsername("newUser");
        dto.setEmail("newuser@example.com");
        dto.setPassword("Password123!");
        dto.setUserGender(UserGender.FEMALE);
        dto.setCountry("Canada");
        return dto;
    }

    private PutUserDto createPutUserDto() {
        PutUserDto dto = new PutUserDto();
        dto.setUsername("updatedUser");
        dto.setEmail("updateduser@example.com");
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setUserGender(UserGender.MALE);
        dto.setCountry("France");
        dto.setLastUpdatedBy(UUID.randomUUID().toString());
        return dto;
    }
}
