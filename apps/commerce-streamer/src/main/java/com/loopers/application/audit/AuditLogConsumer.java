package com.loopers.application.audit;

import com.loopers.config.kafka.KafkaConfig;
import com.loopers.domain.audit.Audit;
import com.loopers.domain.audit.AuditService;
import com.loopers.event.core.EventEnvelop;
import com.loopers.event.core.EventPayload;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuditLogConsumer {
    private final AuditService auditService;

    public AuditLogConsumer(AuditService auditService) {
        this.auditService = auditService;
    }

    @KafkaListener(
            topics = {"catalog-events","order-events"},
            groupId = "audit-log",
            containerFactory = KafkaConfig.BATCH_LISTENER
    )
    public void demoListener(
            List<ConsumerRecord<String, String>> messages,
            Acknowledgment acknowledgment
    ) {
        for (ConsumerRecord<String, String> message : messages) {
            EventEnvelop<EventPayload> envelop = EventEnvelop.fromJson(message.value());
            auditService.save(Audit.of(envelop.getEventId(), envelop.getCreatedAt(), envelop.getType(), envelop.getPayload().toString()));
        }

        acknowledgment.acknowledge(); // manual ack
    }
}
