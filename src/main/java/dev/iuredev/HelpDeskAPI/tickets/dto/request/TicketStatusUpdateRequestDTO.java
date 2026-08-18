package dev.iuredev.HelpDeskAPI.tickets.dto.request;

import dev.iuredev.HelpDeskAPI.enums.TicketStatus;
import jakarta.validation.constraints.NotNull;

public record TicketStatusUpdateRequestDTO(
        @NotNull TicketStatus status
) {
}
