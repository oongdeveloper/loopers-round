package com.loopers.domain.common;

public interface DomainEventPublisher {
    void publish(DomainEvent domainEvent);
}
