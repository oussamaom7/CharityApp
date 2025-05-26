package org.example.charityapp.DTO;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CancelAccountResponse {
    private String message;

    public CancelAccountResponse(String message) {
        this.message = message;
    }
}
