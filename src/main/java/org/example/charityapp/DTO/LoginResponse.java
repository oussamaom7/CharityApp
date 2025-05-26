package org.example.charityapp.DTO;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginResponse {
    private String message;
    private Long userId;

    public LoginResponse(String message, Long userId) {
        this.message = message;
        this.userId = userId;
    }
}
