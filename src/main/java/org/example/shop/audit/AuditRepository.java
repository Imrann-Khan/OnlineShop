package org.example.shop.audit;

import org.springframework.data.jpa.repository.JpaRepository;

interface AuditRepository extends JpaRepository<AuditEntry, Long> {

}