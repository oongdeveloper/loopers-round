package com.loopers.infrastructure.event;

import com.loopers.domain.common.DomainEvent;
import com.loopers.domain.common.DomainEventPublisher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Repository;

@Repository
public class SimpleDomainEventPublisher implements DomainEventPublisher {
    private final ApplicationEventPublisher eventPublisher;

    public SimpleDomainEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void publish(DomainEvent domainEvent) {
        eventPublisher.publishEvent(domainEvent);
    }
}
