package org.example.charityapp.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

public enum UserRole {
    ROLE_USER,
    ROLE_ORGANIZATION,
    ROLE_ADMIN;
    
    public String getAuthority() {
        return name();
    }

    public static UserRole fromString(String role) {
        try {
            return valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("No enum constant " + UserRole.class.getName() + "." + role);
        }
    }
}
