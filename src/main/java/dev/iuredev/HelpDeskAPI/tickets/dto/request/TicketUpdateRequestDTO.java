package dev.iuredev.HelpDeskAPI.tickets.dto.request;

import dev.iuredev.HelpDeskAPI.enums.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;


public record TicketUpdateRequestDTO(
        @NotBlank @Size(max = 255) String title,
        @NotBlank @Size(max = 2000) String description,
        @NotNull Priority priority,
        @NotNull @Positive Long categoryId
) {
}
