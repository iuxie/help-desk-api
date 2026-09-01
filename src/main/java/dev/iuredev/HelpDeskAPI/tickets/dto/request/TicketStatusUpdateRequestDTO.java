package dev.iuredev.HelpDeskAPI.tickets.dto.request;

import dev.iuredev.HelpDeskAPI.enums.TicketStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Dados para alteração do status de um chamado")
public record TicketStatusUpdateRequestDTO(
        @Schema(description = "Novo status permitido pela transição atual", example = "AGUARDANDO_SOLICITANTE")
        @NotNull TicketStatus status
) {
}
