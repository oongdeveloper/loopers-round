package com.loopers.event.producer;

import com.loopers.event.core.EventEnvelop;
import com.loopers.event.producer.domain.DeadEventService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CompletableFuture;

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

            String payload = event.payloadToJson();
            List<RecordHeader> headers = List.of(
                    new RecordHeader("eventType", event.getType().name().getBytes(StandardCharsets.UTF_8)),
                    new RecordHeader("eventId", event.getEventId().getBytes(StandardCharsets.UTF_8)),
                    new RecordHeader("createdAt", String.valueOf(event.getCreatedAt()).getBytes(StandardCharsets.UTF_8))
            );

            ProducerRecord<String, Object> producerRecord = new ProducerRecord<>(event.getTopic(), event.getEventId(), payload);
            headers.forEach(header -> producerRecord.headers().add(header));

            CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(producerRecord);
            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("Kafka 메세지 전송 성공 {}", event.getEventId());
                } else {
                    log.error("Kafka 메세지 전송 실패 {}", event.getEventId());
                    deadEventService.save(event, "Kafka 발행 실패");
                }
            });
    }
}
