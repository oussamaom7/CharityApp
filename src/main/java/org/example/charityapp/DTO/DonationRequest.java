package org.example.charityapp.DTO;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class DonationRequest {
    @DecimalMin(value = "0.01", message = "Amount must be at least 0.01")
    private BigDecimal amount;

    @NotBlank(message = "Donor name is required")
    private String donorName;

    @NotBlank(message = "Charity action name is required")
    private String charityActionName;

    public DonationRequest() {}

    public DonationRequest(BigDecimal amount, String donorName, String charityActionName) {
        this.amount = amount;
        this.donorName = donorName;
        this.charityActionName = charityActionName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDonorName() {
        return donorName;
    }

    public void setDonorName(String donorName) {
        this.donorName = donorName;
    }

    public String getCharityActionName() {
        return charityActionName;
    }

    public void setCharityActionName(String charityActionName) {
        this.charityActionName = charityActionName;
    }
}