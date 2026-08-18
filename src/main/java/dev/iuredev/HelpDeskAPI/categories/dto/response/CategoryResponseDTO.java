package dev.iuredev.HelpDeskAPI.categories.dto.response;

import java.time.LocalDateTime;

public record CategoryResponseDTO(
        Long id,
        String name,
        String description,
        Integer slaDeadlineHours,
        Boolean active,
        LocalDateTime createdAt
) {
}
