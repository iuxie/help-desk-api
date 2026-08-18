package dev.iuredev.HelpDeskAPI.tickets.dto.request;

import dev.iuredev.HelpDeskAPI.categories.model.CategoryModel;
import dev.iuredev.HelpDeskAPI.enums.Priority;
import dev.iuredev.HelpDeskAPI.enums.TicketStatus;
import dev.iuredev.HelpDeskAPI.users.model.UserModel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record TicketCreateRequestDTO(
        @NotBlank @Size(max = 255) String code,
        @NotBlank @Size(max = 255) String title,
        @NotBlank @Size(max = 2000) String description,
        @NotNull @Size(max = 50) Priority priority,
        @NotNull @Size(max = 50) TicketStatus status,
        @NotNull UserModel requester,
        @NotNull LocalDateTime slaDeadline,
        UserModel technician,
        @NotNull CategoryModel category
        ) {
}
