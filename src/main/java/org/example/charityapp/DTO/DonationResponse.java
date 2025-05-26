package org.example.charityapp.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DonationResponse(
        Long id,
        BigDecimal amount,
        String donorName,
        String charityActionName,
        LocalDateTime date,
        String status
) {
}