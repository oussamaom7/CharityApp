package org.example.charityapp.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor
@Entity
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL)
    private List<CharityAction> charityActions;

    public void requestValidation() {
        // Logique de demande de validation
    }

    public void updateInformation() {
        // Logique de mise à jour des informations
    }
}
