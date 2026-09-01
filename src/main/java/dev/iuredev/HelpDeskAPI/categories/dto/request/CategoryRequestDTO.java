package dev.iuredev.HelpDeskAPI.categories.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para criação ou atualização de uma categoria")
public record CategoryRequestDTO(
        @Schema(description = "Nome único da categoria", example = "Hardware")
        @NotBlank(message = "O nome da categoria é obrigatório.")
        @Size(max = 255, message = "O nome da categoria deve possuir no máximo 255 caracteres.")
        String name,

        @Schema(description = "Descrição da categoria", example = "Problemas relacionados aos equipamentos")
        @Size(max = 1000, message = "A descrição deve possuir no máximo 1000 caracteres.")
        String description
) {
}
