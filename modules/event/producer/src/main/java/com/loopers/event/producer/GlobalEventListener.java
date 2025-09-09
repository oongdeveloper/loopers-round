package com.loopers.event.producer;

import com.loopers.event.core.EventEnvelop;
import com.loopers.event.core.EventPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class GlobalEventListener {
    private final EventProducer producer;

    public GlobalEventListener(EventProducer producer) {
        this.producer = producer;
    }

    @Async
    @EventListener
    public void handle(EventPayload payload){
        producer.send(EventEnvelop.of(
                UUID.randomUUID().toString(),
                payload.getType(),
                payload
        ));
    }
}
