package dev.iuredev.HelpDeskAPI.comments.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Representação de um comentário")
public record CommentResponseDTO(
        @Schema(example = "1")
        Long id,
        @Schema(example = "O equipamento será analisado.")
        String message,
        @Schema(example = "false")
        Boolean internal,
        @Schema(example = "2026-08-28T10:30:00")
        LocalDateTime createdAt,
        @Schema(example = "2")
        Long authorId,
        @Schema(example = "1")
        Long ticketId
) {
}
