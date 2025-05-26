package org.example.charityapp.DTO;

import jakarta.validation.constraints.*;

public record CharityActionRequest(
        @NotBlank(message = "Title is required")
        String title,

        String description,

        @NotNull(message = "Organization ID is required")
        Long organizationId,
        
        @NotNull(message = "Goal amount is required")
        @Min(value = 1, message = "Goal amount must be greater than 0")
        Double goalAmount
) {
}