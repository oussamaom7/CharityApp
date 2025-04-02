package org.example.charityapp.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Data @NoArgsConstructor @AllArgsConstructor
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double amount;
    private Date date;
    private String status;

    @OneToOne
    @JoinColumn(name = "donation_id")
    private Donation donation;

    public void validateTransaction() {
        // Logique de validation de la transaction
    }
}
