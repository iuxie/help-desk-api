package dev.iuredev.HelpDeskAPI.users.dto.response;

import dev.iuredev.HelpDeskAPI.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Representação de um usuário")
public record UserResponseDTO(
        @Schema(example = "1")
        Long id,
        @Schema(example = "Maria Silva")
        String name,
        @Schema(example = "maria.silva@email.com")
        String email,
        @Schema(example = "SOLICITANTE")
        Role role,
        @Schema(example = "true")
        Boolean active,
        @Schema(example = "2026-08-28T10:00:00")
        LocalDateTime createdAt,
        @Schema(example = "2026-08-28T11:30:00")
        LocalDateTime updatedAt
) {
}
