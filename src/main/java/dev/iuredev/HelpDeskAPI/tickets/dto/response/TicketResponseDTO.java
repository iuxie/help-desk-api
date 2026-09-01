package dev.iuredev.HelpDeskAPI.tickets.dto.response;

import dev.iuredev.HelpDeskAPI.enums.Priority;
import dev.iuredev.HelpDeskAPI.enums.TicketStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Representação completa de um chamado")
public record TicketResponseDTO(
        @Schema(example = "1")
        Long id,
        @Schema(example = "TKT-550E8400-E29B-41D4-A716-446655440000")
        String code,
        @Schema(example = "Computador não liga")
        String title,
        @Schema(example = "O equipamento não apresenta nenhum sinal.")
        String description,
        @Schema(example = "ALTA")
        Priority priority,
        @Schema(example = "EM_ATENDIMENTO")
        TicketStatus status,
        @Schema(example = "A fonte de alimentação foi substituída.")
        String solution,
        @Schema(example = "2026-08-28T10:00:00")
        LocalDateTime openedAt,
        @Schema(example = "2026-08-28T11:30:00")
        LocalDateTime updatedAt,
        @Schema(example = "2026-08-29T10:00:00")
        LocalDateTime slaDeadline,
        @Schema(example = "2026-08-28T12:00:00")
        LocalDateTime resolvedAt,
        @Schema(example = "1")
        Long requesterId,
        @Schema(example = "2")
        Long technicianId,
        @Schema(example = "1")
        Long categoryId
) {
}
