package dev.iuredev.HelpDeskAPI.tickets.dto.request;

import dev.iuredev.HelpDeskAPI.enums.Priority;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;


@Schema(description = "Dados para abertura de um chamado")
public record TicketCreateRequestDTO(
        @Schema(description = "Título do chamado", example = "Computador não liga")
        @NotBlank @Size(max = 255) String title,
        @Schema(description = "Descrição detalhada do problema", example = "O equipamento não apresenta nenhum sinal.")
        @NotBlank @Size(max = 2000) String description,
        @Schema(description = "Prioridade utilizada no cálculo do SLA", example = "ALTA")
        @NotNull Priority priority,
        @Schema(description = "ID do usuário solicitante", example = "1")
        @NotNull @Positive Long requesterId,
        @Schema(description = "ID da categoria ativa", example = "1")
        @NotNull @Positive Long categoryId
        ) {
}
