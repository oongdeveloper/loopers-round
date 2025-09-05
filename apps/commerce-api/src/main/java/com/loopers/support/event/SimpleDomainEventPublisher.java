package com.loopers.support.event;

import com.loopers.domain.shared.DomainEvent;
import com.loopers.domain.shared.DomainEventPublisher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class SimpleDomainEventPublisher implements DomainEventPublisher {
    private final ApplicationEventPublisher publisher;

    public SimpleDomainEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void publish(DomainEvent domainEvent) {
        publisher.publishEvent(domainEvent);
    }

    @Override
    public void publish(Collection<DomainEvent> domainEvents) {
        domainEvents.forEach(publisher::publishEvent);
    }
}
