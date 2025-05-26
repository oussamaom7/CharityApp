package org.example.charityapp.DTO;

import jakarta.validation.constraints.NotBlank;

public record OrganizationRequest(
        @NotBlank(message = "Name is required")
        String name
) {
}