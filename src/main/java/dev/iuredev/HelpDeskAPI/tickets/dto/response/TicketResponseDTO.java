package dev.iuredev.HelpDeskAPI.tickets.dto.response;

import dev.iuredev.HelpDeskAPI.categories.model.CategoryModel;
import dev.iuredev.HelpDeskAPI.enums.Priority;
import dev.iuredev.HelpDeskAPI.enums.TicketStatus;
import dev.iuredev.HelpDeskAPI.users.model.UserModel;

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
        UserModel requester,
        UserModel technician,
        CategoryModel category
) {
}
