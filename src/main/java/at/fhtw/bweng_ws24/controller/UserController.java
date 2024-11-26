package at.fhtw.bweng_ws24.controller;

import at.fhtw.bweng_ws24.dto.PostUserDto;
import at.fhtw.bweng_ws24.dto.PutUserDto;
import at.fhtw.bweng_ws24.dto.PutUserPasswordDto;
import at.fhtw.bweng_ws24.dto.UserResponseDto;
import at.fhtw.bweng_ws24.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public List<UserResponseDto> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public UserResponseDto getUser(@PathVariable UUID id) {
        return userService.getUser(id);
    }

    @PostMapping()
    public ResponseEntity<?> createUser(@RequestBody @Valid PostUserDto user) {
        UUID userId = userService.createUser(user);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "User created successfully");
        response.put("userId", userId);
        return ResponseEntity
                .created(java.net.URI.create("/users/" + userId))
                .body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasPermission(#id, 'at.fhtw.bweng_ws24.model.User', 'update')")
    public ResponseEntity<?> updateUser(@PathVariable UUID id, @RequestBody @Valid PutUserDto user) {
        userService.updateUser(id, user);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "User updated successfully");
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/password")
    @PreAuthorize("hasPermission(#id, 'at.fhtw.bweng_ws24.model.User', 'update')")
    public ResponseEntity<?> updateUserPassword(@PathVariable UUID id, @RequestBody @Valid PutUserPasswordDto password) {
        userService.updateUserPassword(id, password);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "User password updated successfully");
        return ResponseEntity.ok(response);
    }
}
