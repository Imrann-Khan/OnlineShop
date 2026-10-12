package org.example.shop.user;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    protected User() {}

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    @PrePersist
    void onCreate() {
        this.createdAt = Instant.now();
    }

    // Intention-revealing methods instead of public setters
    public void rename(String name) { this.name = name; }
    public void changeEmail(String email) { this.email = email; }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public Instant getCreatedAt() { return createdAt; }
}
