package dev.iuredev.HelpDeskAPI.users.dto.request;

import jakarta.validation.constraints.NotNull;

public record UserChangeStatusRequestDTO(
        @NotNull Boolean active
) {
}
