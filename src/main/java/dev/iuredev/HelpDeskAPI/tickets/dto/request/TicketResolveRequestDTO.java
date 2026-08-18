package dev.iuredev.HelpDeskAPI.tickets.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TicketResolveRequestDTO(
        @NotBlank @Size(max = 2000) String solution
) {
}
