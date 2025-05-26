package org.example.charityapp.entities;

import jakarta.persistence.*;

@Entity
public class Volunteer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String interest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Volunteer() {}

    public Volunteer(String name, String email, String interest, User user) {
        this.name = name;
        this.email = email;
        this.interest = interest;
        this.user = user;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getInterest() { return interest; }
    public void setInterest(String interest) { this.interest = interest; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
