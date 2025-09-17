package com.loopers.application.metric;

import com.loopers.domain.handled.HandledService;
import com.loopers.event.core.EventSerializer;
import com.loopers.event.core.EventType;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Component;

@Component
public class EventDuplicationService {
    private final HandledService handledService;

    public EventDuplicationService(HandledService handledService) {
        this.handledService = handledService;
    }

    public AggregateEvent validAndConvert(ConsumerRecord<String, byte[]> record){
        if (isAlreadyDone(record.headers().lastHeader("eventId").toString()))
            return null;

        EventType eventType = EventType.from(
                record.headers().lastHeader("eventType").value().toString()
        );
        AggregateEvent event = (AggregateEvent) EventSerializer.deserialize(
                // TODO. byte 처리
                record.value(),
                eventType.getPayloadClass()
        );
        return event;
    }

    private boolean isAlreadyDone(String envelopId){
        return handledService.findByEventId(envelopId)
                .isPresent();
    }
}
