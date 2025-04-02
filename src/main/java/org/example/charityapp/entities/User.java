package org.example.charityapp.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Donation> donations;

    public void register() {
        // Logique d'inscription
    }

    public void login() {
        // Logique de connexion
    }

    public void cancelAccount() {
        // Logique de suppression de compte
    }
}
