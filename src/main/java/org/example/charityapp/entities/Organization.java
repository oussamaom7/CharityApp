package org.example.charityapp.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "organizations")
public class Organization {
    public Organization(String name) {
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(length = 1000)
    private String description;
    
    @Column(nullable = false)
    private String status;
    
    @Column(nullable = false)
    private String contactEmail;
    
    @Column(nullable = false)
    private java.time.LocalDateTime registrationDate;
    
    private String contactPhone;
    private String address;
    
    @Column(unique = true)
    private String registrationNumber;
    
    private String contactPerson;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL)
    private List<CharityAction> charityActions;

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }
    public java.time.LocalDateTime getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(java.time.LocalDateTime registrationDate) { this.registrationDate = registrationDate; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }
    public void setAddress(String address) { this.address = address; }
    public void setRegistrationNumber(String registrationNumber) { this.registrationNumber = registrationNumber; }
    public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }
    public void setUser(User user) { this.user = user; }

    public Long getId() { return id; }
    public List<CharityAction> getCharityActions() { return charityActions; }

    public void requestValidation() {
        // Logique de demande de validation
    }

    public void updateInformation() {
        // Logique de mise à jour des informations
    }
}
