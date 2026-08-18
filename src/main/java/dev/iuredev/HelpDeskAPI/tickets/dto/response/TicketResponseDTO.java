package dev.iuredev.HelpDeskAPI.tickets.dto.response;

import dev.iuredev.HelpDeskAPI.enums.Priority;
import dev.iuredev.HelpDeskAPI.enums.TicketStatus;

import java.time.LocalDateTime;

public record TicketResponseDTO(
        Long id,
        String code,
        String title,
        String description,
        Priority priority,
        TicketStatus status,
        String solution,
        LocalDateTime openedAt,
        LocalDateTime updatedAt,
        LocalDateTime slaDeadline,
        LocalDateTime resolvedAt,
        Long requesterId,
        Long technicianId,
        Long categoryId
) {
}
