package dev.iuredev.HelpDeskAPI.users.controller;

import dev.iuredev.HelpDeskAPI.users.dto.request.UserChangeStatusRequestDTO;
import dev.iuredev.HelpDeskAPI.users.dto.request.UserCreateRequestDTO;
import dev.iuredev.HelpDeskAPI.users.dto.request.UserUpdateRequestDTO;
import dev.iuredev.HelpDeskAPI.users.dto.response.UserResponseDTO;
import dev.iuredev.HelpDeskAPI.users.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> findAllUsers() {
        return ResponseEntity.ok(service.findAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findUserById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findUserById(id));
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserCreateRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.createUser(requestDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequestDTO requestDTO) {
        return ResponseEntity.ok(service.updateUser(id, requestDTO));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<UserResponseDTO> changeUserStatus(
            @PathVariable Long id,
            @Valid @RequestBody UserChangeStatusRequestDTO requestDTO
            ) {
        return ResponseEntity.ok(service.changeUserStatus(id, requestDTO));
    }

}
