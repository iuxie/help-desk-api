package dev.iuredev.HelpDeskAPI.users.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Novo estado de ativação do usuário")
public record UserChangeStatusRequestDTO(
        @Schema(description = "Indica se o usuário deve permanecer ativo", example = "false")
        @NotNull(message = "O status do usuário é obrigatório.")
        Boolean active
) {
}
