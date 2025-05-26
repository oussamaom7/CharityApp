package org.example.charityapp.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private boolean enabled;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Set<UserRole> roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Donation> donations;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Organization organization;

    public User(String username, String email, Set<UserRole> roles, String password) {
        this.username = username;
        this.name = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.enabled = true;
        this.createdAt = LocalDateTime.now();
    }

    public void addRole(UserRole role) {
        if (roles == null) {
            roles = new java.util.HashSet<>();
        }
        roles.add(role);
    }

    public void removeRole(UserRole role) {
        if (roles != null) {
            roles.remove(role);
        }
    }

    public boolean hasRole(UserRole role) {
        return roles != null && roles.contains(role);
    }
}