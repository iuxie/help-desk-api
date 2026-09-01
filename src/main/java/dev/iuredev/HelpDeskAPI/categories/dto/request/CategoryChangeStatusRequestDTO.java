package dev.iuredev.HelpDeskAPI.categories.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Novo estado de ativação da categoria")
public record CategoryChangeStatusRequestDTO(
        @Schema(description = "Indica se a categoria deve permanecer ativa", example = "false")
        @NotNull Boolean active
) {
}
