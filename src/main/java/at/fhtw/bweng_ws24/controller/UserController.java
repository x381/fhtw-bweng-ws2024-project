package at.fhtw.bweng_ws24.controller;

import at.fhtw.bweng_ws24.dto.PostUserDto;
import at.fhtw.bweng_ws24.dto.PutUserDto;
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
    public ResponseEntity<User> createUser(@RequestBody @Valid PostUserDto user) {
        UUID uuid = userService.createUser(user);
        return ResponseEntity
                .created(java.net.URI.create("/users/" + uuid))
                .build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable UUID id, @RequestBody @Valid PutUserDto user) {
        userService.updateUser(id, user);
        return ResponseEntity
                .noContent()
                .build();
    }
}
