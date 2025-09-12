package com.loopers.application.metric;

import com.loopers.config.kafka.KafkaConfig;
import com.loopers.event.core.EventEnvelop;
import com.loopers.event.core.EventPayload;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductMetricConsumer {
    private final ProductMetricFacade metricFacade;
    private final EventDuplicationService duplicationService;

    public ProductMetricConsumer(ProductMetricFacade metricFacade, EventDuplicationService duplicationService) {
        this.metricFacade = metricFacade;
        this.duplicationService = duplicationService;
    }

    @KafkaListener(
            topics = {"catalog-events","order-events"},
            groupId = "product-metric-aggregate",
            containerFactory = KafkaConfig.BATCH_LISTENER
    )
    public void consume(
            List<ConsumerRecord<String, String>> messages,
            Acknowledgment acknowledgment
    ) {
        for (ConsumerRecord<String, String> message : messages) {
            EventEnvelop<EventPayload> envelop = EventEnvelop.fromJson(message.value());
            metricFacade.handle(envelop);
        }

        acknowledgment.acknowledge(); // manual ack
    }

    @KafkaListener(
            topics = {"catalog-events","order-events"},
            groupId = "product-metric-aggregate",
            containerFactory = KafkaConfig.BATCH_LISTENER
    )
    public void batchConsume(
            List<ConsumerRecord<String, byte[]>> messages,
            Acknowledgment acknowledgment
    ) {
        // TODO. eventId 를 어디서 처리할껀데?
        // 이 과정을 별도의 Service 로 빼고, 거기서 바로 검사하는 것도 괜찮을듯?
        List<AggregateEvent> events = new ArrayList<>();
        for (ConsumerRecord<String, byte[]> message : messages) {
            AggregateEvent event = duplicationService.validAndConvert(message);
            if (event != null)
                events.add(event);
        }
        metricFacade.handle(events);

        acknowledgment.acknowledge(); // manual ack
    }
}
