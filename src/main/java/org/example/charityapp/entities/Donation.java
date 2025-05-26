package org.example.charityapp.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Donation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private java.math.BigDecimal amount;
    private String donorName;
    private String paymentMethod;
    private java.time.LocalDateTime date;
    private String status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "charity_action_id")
    private CharityAction charityAction;

    public Donation(java.math.BigDecimal amount, String donorName, CharityAction charityAction) {
        this.amount = amount;
        this.donorName = donorName;
        this.charityAction = charityAction;
    }

    public void processPayment() {
        // Logique de traitement du paiement
    }

    public void cancelDonation() {
        // Logique d'annulation du don
    }
}

