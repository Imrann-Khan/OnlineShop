package org.example.shop.audit;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditService {

    private final AuditRepository repository;

    public AuditService(AuditRepository repository) {
        this.repository = repository;
    }

    // Its own transaction: the audit row survives even if the caller's transaction rolls back
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void record(String action, String details) {
        repository.save(new AuditEntry(action, details));
    }
}
