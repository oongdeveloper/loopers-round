package com.loopers.domain.shared;

import java.util.Collection;

public interface DomainEventPublisher {
    void publish(DomainEvent domainEvent);
    void publish(Collection<DomainEvent> domainEvents);
}
