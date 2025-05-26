package org.example.charityapp.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record OrganizationRegistrationRequest(
    @NotBlank(message = "Organization name is required")
    @Size(max = 100, message = "Name must be less than 100 characters")
    String name,
    
    @NotBlank(message = "Description is required")
    @Size(max = 1000, message = "Description must be less than 1000 characters")
    String description,
    
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    String email,
    
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    String password,
    
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9+\\(\\)\\-\\s]+$", message = "Invalid phone number format")
    String phone,
    
    @NotBlank(message = "Address is required")
    @Size(max = 255, message = "Address must be less than 255 characters")
    String address,
    
    @NotBlank(message = "Registration number is required")
    @Size(max = 50, message = "Registration number must be less than 50 characters")
    String registrationNumber,
    
    @NotBlank(message = "Contact person name is required")
    @Size(max = 100, message = "Contact person name must be less than 100 characters")
    String contactPerson
) {}
