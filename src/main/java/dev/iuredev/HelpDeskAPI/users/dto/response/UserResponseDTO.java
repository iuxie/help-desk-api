package dev.iuredev.HelpDeskAPI.users.dto.response;

import dev.iuredev.HelpDeskAPI.enums.Role;

import java.time.LocalDateTime;

public record UserResponseDTO(
        Long id,
        String name,
        String email,
        Role role,
        Boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
