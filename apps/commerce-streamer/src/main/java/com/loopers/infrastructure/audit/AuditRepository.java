package com.loopers.infrastructure.audit;

import com.loopers.domain.audit.Audit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditRepository extends JpaRepository<Audit, Long> {
}
