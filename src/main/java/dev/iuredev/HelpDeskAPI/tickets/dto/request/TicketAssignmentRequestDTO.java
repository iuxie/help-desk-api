package dev.iuredev.HelpDeskAPI.tickets.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "Dados para atribuição de um técnico")
public record TicketAssignmentRequestDTO(
        @Schema(description = "ID de um usuário ativo com perfil TECNICO", example = "2")
        @NotNull @Positive Long technicianId
) {
}
