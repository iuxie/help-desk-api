package dev.iuredev.HelpDeskAPI.tickets.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Dados para resolução de um chamado")
public record TicketResolveRequestDTO(
        @Schema(description = "Solução aplicada ao chamado", example = "A fonte de alimentação foi substituída.")
        @NotBlank @Size(max = 2000) String solution
) {
}
