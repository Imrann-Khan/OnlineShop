package org.example.shop.audit;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "audit_log")
public class AuditEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String action;

    @Column(length = 500)
    private String details;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected AuditEntry() {}

    AuditEntry(String action, String details) {
        this.action = action;
        this.details = details;
        this.createdAt = Instant.now();
    }
}
