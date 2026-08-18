package dev.iuredev.HelpDeskAPI.categories.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryRequestDTO(
        @NotBlank @Size(max = 255) String name,
        @Size(max = 1000) String description
) {
}
