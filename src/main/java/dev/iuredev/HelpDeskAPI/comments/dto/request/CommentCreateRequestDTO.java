package dev.iuredev.HelpDeskAPI.comments.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CommentCreateRequestDTO(
        @NotBlank @Size(max = 1000) String message,
        @NotNull Boolean internal,
        @NotNull @Positive Long authorId,
        @NotNull @Positive Long ticketId
) {
}
