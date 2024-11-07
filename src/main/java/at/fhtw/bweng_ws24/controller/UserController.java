package at.fhtw.bweng_ws24.controller;

import at.fhtw.bweng_ws24.dto.CreateUserDto;
import at.fhtw.bweng_ws24.model.User;
import at.fhtw.bweng_ws24.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable UUID id) {
        return userService.getUser(id);
    }

    @PostMapping()
    public ResponseEntity<User> createUser(@RequestBody @Valid CreateUserDto user) {
        UUID uuid = userService.createUser(user);
        return ResponseEntity
                .created(java.net.URI.create("/users/" + uuid))
                .build();
    }
}
