package org.example.charityapp.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank String username,
        @NotBlank String email,
        @NotBlank @Size(min = 6, message = "Password must be at least 6 characters") String password,
        @NotBlank String confirmPassword,
        @NotBlank String role,
        // Organization fields (optional)
        String organizationName,
        String registrationNumber,
        String description
) {
    // Constructor without organization fields for regular users
    public RegisterRequest(String username, String email, String password, String confirmPassword, String role) {
        this(username, email, password, confirmPassword, role, null, null, null);
    }
}