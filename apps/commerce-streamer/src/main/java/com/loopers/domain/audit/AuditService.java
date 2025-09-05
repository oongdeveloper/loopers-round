package com.loopers.domain.audit;

import com.loopers.infrastructure.audit.AuditRepository;
import org.springframework.stereotype.Service;

@Service
public class AuditService {
    private final AuditRepository auditRepository;

    public AuditService(AuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    public void save(Audit audit){
        auditRepository.save(audit);
    }
}
