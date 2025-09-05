package com.loopers.event.producer;

import com.loopers.event.core.EventEnvelop;
import com.loopers.event.core.EventPayload;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class EventStore {
    private final EventProducer producer;

    public EventStore(EventProducer producer) {
        this.producer = producer;
    }

    public void store(EventPayload payload){
        producer.send(
                EventEnvelop.of(
                    UUID.randomUUID().toString(),
                    payload.getType(),
                    payload
        ));
    }
}
