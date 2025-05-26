package org.example.charityapp.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * This document stores user behavior events for analytics and auditing.
 * Flexible and extensible: you can add any event type or metadata you want!
 */
@Entity
@Table(name = "user_behaviors")
public class UserBehavior {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String actionType;
    private String actionDetails;
    private LocalDateTime timestamp;
    private String ipAddress;
    private String userAgent;

    public UserBehavior() {}

    public UserBehavior(User user, String actionType, String actionDetails, LocalDateTime timestamp, String ipAddress, String userAgent) {
        this.user = user;
        this.actionType = actionType;
        this.actionDetails = actionDetails;
        this.timestamp = timestamp;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getActionDetails() {
        return actionDetails;
    }

    public void setActionDetails(String actionDetails) {
        this.actionDetails = actionDetails;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
}
