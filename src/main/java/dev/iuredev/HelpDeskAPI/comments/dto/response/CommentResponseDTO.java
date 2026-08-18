package dev.iuredev.HelpDeskAPI.comments.dto.response;

import java.time.LocalDateTime;

public record CommentResponseDTO(
        Long id,
        String message,
        Boolean internal,
        LocalDateTime createdAt,
        Long authorId,
        Long ticketId
) {
}
