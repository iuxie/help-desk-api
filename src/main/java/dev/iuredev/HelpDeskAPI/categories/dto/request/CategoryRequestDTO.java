package dev.iuredev.HelpDeskAPI.categories.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryRequestDTO(
        @NotBlank(message = "O nome da categoria é obrigatório.")
        @Size(max = 255, message = "O nome da categoria deve possuir no máximo 255 caracteres.")
        String name,

        @Size(max = 1000, message = "A descrição deve possuir no máximo 1000 caracteres.")
        String description
) {
}
