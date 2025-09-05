package com.loopers.event.producer;

import com.loopers.event.core.EventEnvelop;
import com.loopers.event.producer.domain.DeadEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
@Component
class EventProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final DeadEventService deadEventService;

    public EventProducer(KafkaTemplate<String, Object> kafkaTemplate, DeadEventService deadEventService) {
        this.kafkaTemplate = kafkaTemplate;
        this.deadEventService = deadEventService;
    }

    public void send(EventEnvelop<?> event){
        try {
            kafkaTemplate.send(event.getTopic(), event.getEventId(), event.toJson())
                    .get(1L, TimeUnit.MINUTES);
        } catch (InterruptedException | ExecutionException e) {
            log.info("Kafka 이벤트 발행 실패 {}", event);
            deadEventService.save(event, "Kafka 발행 실패 오류");
            throw new RuntimeException(e);
        } catch (TimeoutException e){
            log.info("Kafka 이벤트 발행 Timeout 발생 {}", event);
            deadEventService.save(event, "Kafka 발행 Timeout");
            throw new RuntimeException(e);
        }
    }
}
