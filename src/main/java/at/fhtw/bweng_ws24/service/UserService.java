package at.fhtw.bweng_ws24.service;

import at.fhtw.bweng_ws24.dto.CreateUserDto;
import at.fhtw.bweng_ws24.model.User;
import at.fhtw.bweng_ws24.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getUser(UUID id) {
        return userRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("User with id " + id + " not found.")
        );
    }

    public UUID createUser(CreateUserDto user) {
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setUserGender(user.getUserGender());
        newUser.setOtherSpecify(user.getOtherSpecify());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(user.getPassword());
        newUser.setCountry(user.getCountry());
        return userRepository.save(newUser).getId();
    }
}