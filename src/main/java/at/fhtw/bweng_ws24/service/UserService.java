package at.fhtw.bweng_ws24.service;

import at.fhtw.bweng_ws24.dto.PostUserDto;
import at.fhtw.bweng_ws24.dto.PutUserDto;
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

    public UUID createUser(PostUserDto user) {
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setUserGender(user.getUserGender());
        newUser.setOtherSpecify(user.getOtherSpecify());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(user.getPassword());
        newUser.setEnabled(true);
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
        updatedUser.setUsername(user.getUsername());
        updatedUser.setFirstName(user.getFirstName());
        updatedUser.setLastName(user.getLastName());
        updatedUser.setUserGender(user.getUserGender());
        updatedUser.setOtherSpecify(user.getOtherSpecify());
        updatedUser.setEmail(user.getEmail());
        updatedUser.setPassword(user.getPassword());
        updatedUser.setCountry(user.getCountry());
        updatedUser.setCity(user.getCity());
        updatedUser.setStreet(user.getStreet());
        updatedUser.setZip(user.getZip());
        updatedUser.setPhone(user.getPhone());
        updatedUser.setImage(user.getImage());
        updatedUser.setLastUpdatedBy(UUID.fromString(user.getLastUpdatedBy()));
        userRepository.save(updatedUser);
    }
}