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
    private Double currentAmount;
    private String status;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @OneToMany(mappedBy = "charityAction", cascade = CascadeType.ALL)
    private List<Donation> donations;

    public CharityAction(String title, String description, Organization organization) {
        this.title = title;
        this.description = description;
        this.organization = organization;
    }

    public Double getCurrentAmount() { return currentAmount; }
    public void setCurrentAmount(Double currentAmount) { this.currentAmount = currentAmount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public void setGoalAmount(Double goalAmount) { this.goalAmount = goalAmount; }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Double getGoalAmount() { return goalAmount; }
    public Organization getOrganization() { return organization; }
    public List<Donation> getDonations() { return donations; }

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
