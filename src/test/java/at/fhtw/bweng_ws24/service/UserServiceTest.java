package at.fhtw.bweng_ws24.service;

import at.fhtw.bweng_ws24.dto.PostUserDto;
import at.fhtw.bweng_ws24.dto.PutUserDto;
import at.fhtw.bweng_ws24.dto.PutUserPasswordDto;
import at.fhtw.bweng_ws24.dto.UserResponseDto;
import at.fhtw.bweng_ws24.exception.EmailExistsException;
import at.fhtw.bweng_ws24.exception.PasswordWrongException;
import at.fhtw.bweng_ws24.exception.UsernameExistsException;
import at.fhtw.bweng_ws24.mapper.UserMapper;
import at.fhtw.bweng_ws24.model.User;
import at.fhtw.bweng_ws24.model.UserGender;
import at.fhtw.bweng_ws24.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ResourceImageService resourceImageService;

    @InjectMocks
    private UserService userService;

    private UUID sampleUserId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleUserId = UUID.randomUUID();
    }

    @Test
    void testGetUsers_ReturnsListOfUsers() {
        // Arrange
        User user = new User();
        user.setId(sampleUserId);
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(userMapper.toUserResponseDto(any(User.class))).thenReturn(new UserResponseDto());

        // Act
        List<UserResponseDto> users = userService.getUsers();

        // Assert
        assertThat(users).isNotNull();
        assertThat(users.size()).isEqualTo(1);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetUserResponseDto_ReturnsUserResponseDto() {
        // Arrange
        User user = new User();
        user.setId(sampleUserId);
        when(userRepository.findById(sampleUserId)).thenReturn(Optional.of(user));
        when(userMapper.toUserResponseDto(user)).thenReturn(new UserResponseDto());

        // Act
        UserResponseDto userResponseDto = userService.getUserResponseDto(sampleUserId);

        // Assert
        assertNotNull(userResponseDto);
        verify(userRepository, times(1)).findById(sampleUserId);
    }

    @Test
    void testGetUserResponseDto_ThrowException_WhenUserNotFound() {
        // Arrange
        when(userRepository.findById(sampleUserId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> userService.getUserResponseDto(sampleUserId));
    }

    @Test
    void testGetUser_ThrowsException_WhenUserNotFound() {
        // Arrange
        when(userRepository.findById(sampleUserId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> userService.getUser(sampleUserId));
    }

    @Test
    void testCreateUser_Successful() throws EmailExistsException {
        // Arrange
        PostUserDto postUserDto = new PostUserDto();
        postUserDto.setUsername("testUser");
        postUserDto.setEmail("test@example.com");
        postUserDto.setPassword("Strong@Password123");
        postUserDto.setCountry("Austria");
        postUserDto.setUserGender(UserGender.MALE);

        User savedUser = new User();
        savedUser.setId(sampleUserId);

        when(userRepository.findByEmail(postUserDto.getEmail())).thenReturn(null);
        when(userRepository.findByUsername(postUserDto.getUsername())).thenReturn(null);
        when(passwordEncoder.encode(postUserDto.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        UUID createdUserId = userService.createUser(postUserDto);

        // Assert
        assertEquals(sampleUserId, createdUserId);
        verify(userRepository, times(2)).save(any(User.class)); // save called twice
    }

    @Test
    void testCreateUser_ThrowsException_WhenEmailExists() {
        // Arrange
        PostUserDto postUserDto = new PostUserDto();
        postUserDto.setEmail("test@example.com");

        when(userRepository.findByEmail(postUserDto.getEmail())).thenReturn(new User());

        // Act & Assert
        assertThrows(EmailExistsException.class, () -> userService.createUser(postUserDto));
    }

    @Test
    void testCreateUser_ThrowsException_WhenUsernameExists() {
        // Arrange
        PostUserDto postUserDto = new PostUserDto();
        postUserDto.setUsername("testUser");

        when(userRepository.findByUsername(postUserDto.getUsername())).thenReturn(new User());

        // Act & Assert
        assertThrows(UsernameExistsException.class, () -> userService.createUser(postUserDto));
    }

    @Test
    void testUpdateUser_ThrowsException_WhenUserNotFound() {
        // Arrange
        PutUserDto putUserDto = new PutUserDto();
        when(userRepository.findById(sampleUserId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> userService.updateUser(sampleUserId, putUserDto));
    }

    @Test
    void testUpdateUser_Successful() {
        // Arrange
        PutUserDto putUserDto = new PutUserDto();
        putUserDto.setUsername("newUsername");
        putUserDto.setEmail("newEmail@example.com");
        putUserDto.setLastUpdatedBy(String.valueOf(sampleUserId));

        User existingUser = new User();
        existingUser.setId(sampleUserId);
        existingUser.setEmail("oldEmail@example.com");
        existingUser.setUsername("oldUsername");

        when(userRepository.findById(sampleUserId)).thenReturn(Optional.of(existingUser));
        when(userRepository.findByEmail(putUserDto.getEmail())).thenReturn(null);
        when(userRepository.findByUsername(putUserDto.getUsername())).thenReturn(null);

        // Act
        userService.updateUser(sampleUserId, putUserDto);

        // Assert
        assertEquals("newUsername", existingUser.getUsername());
        assertEquals("newEmail@example.com", existingUser.getEmail());
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void testUpdateUser_ThrowsException_WhenEmailExists() {
        // Arrange
        PutUserDto putUserDto = new PutUserDto();
        putUserDto.setEmail("existingEmail@example.com");
        putUserDto.setLastUpdatedBy(String.valueOf(sampleUserId));

        User existingUser = new User();
        existingUser.setId(sampleUserId);
        existingUser.setEmail("email@example.com");

        when(userRepository.findById(sampleUserId)).thenReturn(Optional.of(existingUser));
        when(userRepository.findByEmail(putUserDto.getEmail())).thenReturn(new User());

        // Act & Assert
        assertThrows(EmailExistsException.class, () -> userService.updateUser(sampleUserId, putUserDto));
    }

    @Test
    void testUpdateUser_ThrowsException_WhenUsernameExists() {
        // Arrange
        PutUserDto putUserDto = new PutUserDto();
        putUserDto.setUsername("existingUsername");
        putUserDto.setLastUpdatedBy(String.valueOf(sampleUserId));

        User existingUser = new User();
        existingUser.setId(sampleUserId);
        existingUser.setUsername("username");

        when(userRepository.findById(sampleUserId)).thenReturn(Optional.of(existingUser));
        when(userRepository.findByUsername(putUserDto.getUsername())).thenReturn(new User());

        // Act & Assert
        assertThrows(UsernameExistsException.class, () -> userService.updateUser(sampleUserId, putUserDto));
    }

    @Test
    void testUpdateUserPassword() {
        // Arrange
        PutUserPasswordDto passwordDto = new PutUserPasswordDto();
        passwordDto.setActualPassword("oldPassword");
        passwordDto.setNewPassword("New@Password123");
        passwordDto.setLastUpdatedBy(String.valueOf(sampleUserId));


        User existingUser = new User();
        existingUser.setPassword("encodedPassword");

        when(userRepository.findById(sampleUserId)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches(passwordDto.getActualPassword(), existingUser.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(passwordDto.getNewPassword())).thenReturn("encodedNewPassword");

        // Act
        userService.updateUserPassword(sampleUserId, passwordDto);

        // Assert
        assertEquals("encodedNewPassword", existingUser.getPassword());
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void testUpdateUserPassword_ThrowsException_WhenPasswordWrong() {
        // Arrange
        PutUserPasswordDto passwordDto = new PutUserPasswordDto();
        passwordDto.setActualPassword("wrongPassword");
        passwordDto.setNewPassword("New@Password123");

        User existingUser = new User();
        existingUser.setPassword("encodedPassword");

        when(userRepository.findById(sampleUserId)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches(passwordDto.getActualPassword(), existingUser.getPassword())).thenReturn(false);

        // Act & Assert
        assertThrows(PasswordWrongException.class, () -> userService.updateUserPassword(sampleUserId, passwordDto));
    }

    @Test
    void testUpdateUserPassword_ThrowsException_WhenUserNotFound() {
        // Arrange
        PutUserPasswordDto passwordDto = new PutUserPasswordDto();
        when(userRepository.findById(sampleUserId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> userService.updateUserPassword(sampleUserId, passwordDto));
    }

    @Test
    void testDeleteUser_SuccessfulWithImage() {
        // Arrange
        User user = new User();
        user.setId(sampleUserId);
        user.setImage("a051b327-3bac-4d7d-8155-be6340874d96");

        when(userRepository.findById(sampleUserId)).thenReturn(Optional.of(user));

        // Act
        userService.deleteUser(sampleUserId);

        // Assert
        verify(resourceImageService, times(1)).deleteResourceImage(UUID.fromString("a051b327-3bac-4d7d-8155-be6340874d96"));
        verify(userRepository, times(1)).deleteById(sampleUserId);
    }

    @Test
    void testDeleteUser_SuccessfulWithoutImage() {
        // Arrange
        User user = new User();
        user.setId(sampleUserId);

        when(userRepository.findById(sampleUserId)).thenReturn(Optional.of(user));

        // Act
        userService.deleteUser(sampleUserId);

        // Assert
        verify(resourceImageService, never()).deleteResourceImage(any(UUID.class));
        verify(userRepository, times(1)).deleteById(sampleUserId);
    }

    @Test
    void testDeleteUser_ThrowsException_WhenUserNotFound() {
        // Arrange
        when(userRepository.findById(sampleUserId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> userService.deleteUser(sampleUserId));
    }
}