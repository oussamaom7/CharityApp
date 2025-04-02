package org.example.charityapp.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Data @NoArgsConstructor @AllArgsConstructor
@Entity
public class Donation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double amount;
    private String paymentMethod;
    private Date date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "charity_action_id")
    private CharityAction charityAction;

    public void processPayment() {
        // Logique de traitement du paiement
    }

    public void cancelDonation() {
        // Logique d'annulation du don
    }
}
