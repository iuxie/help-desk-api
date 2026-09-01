package dev.iuredev.HelpDeskAPI.comments.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para criação de um comentário")
public record CommentCreateRequestDTO(
        @Schema(description = "Conteúdo do comentário", example = "O equipamento será analisado.")
        @NotBlank @Size(max = 1000) String message,
        @Schema(description = "Indica se o comentário é visível somente para a equipe interna", example = "false")
        @NotNull Boolean internal,
        @Schema(description = "ID do autor do comentário", example = "2")
        @NotNull @Positive Long authorId,
        @Schema(description = "ID do chamado", example = "1")
        @NotNull @Positive Long ticketId
) {
}
