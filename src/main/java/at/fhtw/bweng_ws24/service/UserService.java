package at.fhtw.bweng_ws24.service;

import at.fhtw.bweng_ws24.dto.PostUserDto;
import at.fhtw.bweng_ws24.dto.PutUserDto;
import at.fhtw.bweng_ws24.dto.UserResponseDto;
import at.fhtw.bweng_ws24.exception.EmailExistsException;
import at.fhtw.bweng_ws24.exception.UsernameExistsException;
import at.fhtw.bweng_ws24.mapper.UserMapper;
import at.fhtw.bweng_ws24.model.User;
import at.fhtw.bweng_ws24.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserResponseDto> getUsers() {
        return this.userRepository
                .findAll().stream()
                .map(userMapper::toUserResponseDto)
                .toList();
    }

    public UserResponseDto getUser(UUID id) {
        return userMapper.toUserResponseDto(userRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("User with id " + id + " not found.")
        ));
    }

    public UUID createUser(PostUserDto user) throws EmailExistsException {
        if (emailExist(user.getEmail())) {
            throw new EmailExistsException(
                    "There is an account with that email adress:" + user.getEmail());
        }
        if (usernameExist(user.getUsername())) {
            throw new UsernameExistsException(
                    "There is an account with that username:" + user.getUsername());
        }
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setUserGender(user.getUserGender());
        newUser.setOtherSpecify(user.getOtherSpecify());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setCountry(user.getCountry());
        UUID uuid = userRepository.save(newUser).getId();
        newUser.setLastUpdatedBy(uuid);
        userRepository.save(newUser);
        return uuid;
    }

    public void updateUser(UUID id, PutUserDto user) {
        User updatedUser = userRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("User with id " + id + " not found.")
        );
        if (emailExist(user.getEmail())) {
            throw new EmailExistsException(
                    "There is an account with that email adress:" + user.getEmail());
        }
        if (usernameExist(user.getUsername())) {
            throw new UsernameExistsException(
                    "There is an account with that username:" + user.getUsername());
        }
        updatedUser.setUsername(user.getUsername());
        updatedUser.setFirstName(user.getFirstName());
        updatedUser.setLastName(user.getLastName());
        updatedUser.setUserGender(user.getUserGender());
        updatedUser.setOtherSpecify(user.getOtherSpecify());
        updatedUser.setEmail(user.getEmail());
        updatedUser.setPassword(passwordEncoder.encode(user.getPassword()));
        updatedUser.setCountry(user.getCountry());
        updatedUser.setCity(user.getCity());
        updatedUser.setStreet(user.getStreet());
        updatedUser.setZip(user.getZip());
        updatedUser.setPhone(user.getPhone());
        updatedUser.setImage(user.getImage());
        updatedUser.setLastUpdatedBy(UUID.fromString(user.getLastUpdatedBy()));
        userRepository.save(updatedUser);
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public boolean emailExist(String email) {
        return userRepository.findByEmail(email) != null;
    }

    public boolean usernameExist(String username) {
        return userRepository.findByUsername(username) != null;
    }
}