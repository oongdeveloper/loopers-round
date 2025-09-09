package com.loopers.domain.shared;

import com.loopers.domain.BaseAuditableEntity;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@MappedSuperclass
public class AuditableAggregateRoot extends BaseAuditableEntity {
    @Transient
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    protected void registerEvent(DomainEvent event) {
        this.domainEvents.add(event);
    }

    public List<DomainEvent> pullDomainEvents() {
//        List<DomainEvent> events = new ArrayList<>(this.domainEvents);
        List<DomainEvent> events = this.domainEvents.stream()
                .collect(Collectors.toList());
        this.domainEvents.clear();
        return events;
    }

    public boolean hasDomainEvents() {
        return !this.domainEvents.isEmpty();
    }

    public void clearDomainEvents() {
        this.domainEvents.clear();
    }
}
