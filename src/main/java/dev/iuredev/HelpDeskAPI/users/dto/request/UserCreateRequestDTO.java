package dev.iuredev.HelpDeskAPI.users.dto.request;

import dev.iuredev.HelpDeskAPI.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserCreateRequestDTO(
        @NotBlank(message = "O nome é obrigatório.")
        @Size(max = 255, message = "O nome deve possuir no máximo 255 caracteres.")
        String name,

        @NotBlank(message = "O e-mail é obrigatório.")
        @Email(message = "O e-mail informado é inválido.")
        @Size(max = 255, message = "O e-mail deve possuir no máximo 255 caracteres.")
        String email,

        @NotBlank(message = "A senha é obrigatória.")
        @Size(min = 8, max = 64, message = "A senha deve possuir entre 8 e 64 caracteres.")
        String password,

        @NotNull(message = "O perfil do usuário é obrigatório.")
        Role role
) {
}
