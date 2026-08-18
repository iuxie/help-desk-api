package dev.iuredev.HelpDeskAPI.comments.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CommentUpdateRequestDTO(
        @NotBlank @Size(max = 1000) String message,
        @NotNull Boolean internal
) {
}
