package dev.iuredev.HelpDeskAPI.tickets.dto.request;

import dev.iuredev.HelpDeskAPI.enums.Priority;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;


@Schema(description = "Dados para atualização de um chamado")
public record TicketUpdateRequestDTO(
        @Schema(description = "Título do chamado", example = "Computador desliga sozinho")
        @NotBlank @Size(max = 255) String title,
        @Schema(description = "Descrição detalhada do problema", example = "O equipamento desliga após alguns minutos de uso.")
        @NotBlank @Size(max = 2000) String description,
        @Schema(description = "Prioridade utilizada no cálculo do SLA", example = "MEDIA")
        @NotNull Priority priority,
        @Schema(description = "ID da categoria ativa", example = "1")
        @NotNull @Positive Long categoryId
) {
}
