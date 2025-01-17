package at.fhtw.bweng_ws24.mapper;

import at.fhtw.bweng_ws24.dto.UserResponseDto;
import at.fhtw.bweng_ws24.model.User;
import at.fhtw.bweng_ws24.model.UserGender;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    void shouldMapUserToUserResponseDto() {
        UUID userId = UUID.randomUUID();
        Instant createdAt = Instant.now();

        User user = new User();
        user.setId(userId);
        user.setUsername("testuser");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setUserGender(UserGender.MALE);
        user.setEmail("john.doe@example.com");
        user.setCountry("Austria");
        user.setCity("Vienna");
        user.setStreet("Main Street 123");
        user.setZip("1010");
        user.setPhone("+43123456789");
        user.setImage("image-id");
        user.setRole("ROLE_ADMIN");
        user.setCreatedAt(createdAt);

        UserResponseDto userResponseDto = userMapper.toUserResponseDto(user);

        assertThat(userResponseDto).isNotNull();
        assertThat(userResponseDto.getId()).isEqualTo(userId);
        assertThat(userResponseDto.getUsername()).isEqualTo("testuser");
        assertThat(userResponseDto.getFirstName()).isEqualTo("John");
        assertThat(userResponseDto.getLastName()).isEqualTo("Doe");
        assertThat(userResponseDto.getUserGender()).isEqualTo(UserGender.MALE);
        assertThat(userResponseDto.getEmail()).isEqualTo("john.doe@example.com");
        assertThat(userResponseDto.getCountry()).isEqualTo("Austria");
        assertThat(userResponseDto.getCity()).isEqualTo("Vienna");
        assertThat(userResponseDto.getStreet()).isEqualTo("Main Street 123");
        assertThat(userResponseDto.getZip()).isEqualTo("1010");
        assertThat(userResponseDto.getPhone()).isEqualTo("+43123456789");
        assertThat(userResponseDto.getImage()).isEqualTo("image-id");
        assertThat(userResponseDto.getRole()).isEqualTo("ROLE_ADMIN");
        assertThat(userResponseDto.getCreatedAt()).isEqualTo(createdAt);
    }
}