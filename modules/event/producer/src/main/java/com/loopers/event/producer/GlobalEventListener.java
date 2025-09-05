package com.loopers.event.producer;

import com.loopers.event.core.EventEnvelop;
import com.loopers.event.core.EventPayload;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.UUID;

@Component
public class GlobalEventListener {
    private final EventProducer producer;

    public GlobalEventListener(EventProducer producer) {
        this.producer = producer;
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(EventPayload payload){
        producer.send(EventEnvelop.of(
                UUID.randomUUID().toString(),
                payload.getType(),
                payload
        ));
    }
}
