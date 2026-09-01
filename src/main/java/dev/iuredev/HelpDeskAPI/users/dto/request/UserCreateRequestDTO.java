package dev.iuredev.HelpDeskAPI.users.dto.request;

import dev.iuredev.HelpDeskAPI.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para criação de um usuário")
public record UserCreateRequestDTO(
        @Schema(description = "Nome completo do usuário", example = "Maria Silva")
        @NotBlank(message = "O nome é obrigatório.")
        @Size(max = 255, message = "O nome deve possuir no máximo 255 caracteres.")
        String name,

        @Schema(description = "E-mail único do usuário", example = "maria.silva@email.com")
        @NotBlank(message = "O e-mail é obrigatório.")
        @Email(message = "O e-mail informado é inválido.")
        @Size(max = 255, message = "O e-mail deve possuir no máximo 255 caracteres.")
        String email,

        @Schema(description = "Senha com 8 a 64 caracteres", example = "senha123", format = "password", accessMode = Schema.AccessMode.WRITE_ONLY)
        @NotBlank(message = "A senha é obrigatória.")
        @Size(min = 8, max = 64, message = "A senha deve possuir entre 8 e 64 caracteres.")
        String password,

        @Schema(description = "Perfil do usuário", example = "SOLICITANTE")
        @NotNull(message = "O perfil do usuário é obrigatório.")
        Role role
) {
}
