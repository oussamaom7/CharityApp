package org.example.charityapp.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CancelAccountRequest {

    @NotBlank(message = "Email is required")
    private String email;
}