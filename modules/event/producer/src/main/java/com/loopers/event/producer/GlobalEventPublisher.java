package com.loopers.event.producer;

import com.loopers.event.core.EventPayload;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class GlobalEventPublisher {
    private final ApplicationEventPublisher publisher;

    public GlobalEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void publish(EventPayload payload){
        publisher.publishEvent(payload);
    }
}
