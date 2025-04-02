package org.example.charityapp.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor
@Entity
public class CharityAction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private Double goalAmount;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @OneToMany(mappedBy = "charityAction", cascade = CascadeType.ALL)
    private List<Donation> donations;

    public void createAction() {
        // Logique de création d'une action
    }

    public void updateStatus() {
        // Logique de mise à jour du statut
    }

    public void getActionDetails() {
        // Logique pour récupérer les détails
    }
}
