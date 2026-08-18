package dev.iuredev.HelpDeskAPI.tickets.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TicketAssignmentRequestDTO(
        @NotNull @Positive Long technicianId
) {
}
