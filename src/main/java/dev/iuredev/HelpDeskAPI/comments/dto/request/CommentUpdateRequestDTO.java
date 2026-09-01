package dev.iuredev.HelpDeskAPI.comments.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para atualização de um comentário")
public record CommentUpdateRequestDTO(
        @Schema(description = "Novo conteúdo do comentário", example = "O equipamento está em análise.")
        @NotBlank @Size(max = 1000) String message
) {
}
