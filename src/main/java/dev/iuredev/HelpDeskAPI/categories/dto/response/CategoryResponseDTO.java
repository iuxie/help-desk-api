package dev.iuredev.HelpDeskAPI.categories.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Representação de uma categoria")
public record CategoryResponseDTO(
        @Schema(example = "1")
        Long id,
        @Schema(example = "HARDWARE")
        String name,
        @Schema(example = "Problemas relacionados aos equipamentos")
        String description,
        @Schema(example = "true")
        Boolean active,
        @Schema(example = "2026-08-28T10:00:00")
        LocalDateTime createdAt
) {
}
