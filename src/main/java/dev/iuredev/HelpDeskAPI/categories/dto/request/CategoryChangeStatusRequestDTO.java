package dev.iuredev.HelpDeskAPI.categories.dto.request;

import jakarta.validation.constraints.NotNull;

public record CategoryChangeStatusRequestDTO(
        @NotNull Boolean active
) {
}
