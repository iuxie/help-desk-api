package dev.iuredev.HelpDeskAPI.users.dto.request;

import dev.iuredev.HelpDeskAPI.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserCreateRequestDTO(
        @NotBlank @Size(max = 255) String name,
        @NotBlank @Email @Size(max = 255) String email,
        @NotBlank String password,
        @NotNull Role role
) {
}
