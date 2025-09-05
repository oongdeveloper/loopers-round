package com.loopers.event.producer;

import com.loopers.event.core.EventPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
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
