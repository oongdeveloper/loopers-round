package com.loopers.application.metric;

import com.loopers.config.kafka.KafkaConfig;
import com.loopers.event.core.EventEnvelop;
import com.loopers.event.core.EventPayload;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductMetricConsumer {
    private final ProductMetricFacade metricFacade;

    public ProductMetricConsumer(ProductMetricFacade metricFacade) {
        this.metricFacade = metricFacade;
    }

    @KafkaListener(
            topics = {"catalog-events","order-events"},
            groupId = "product-metric-aggregate",
            containerFactory = KafkaConfig.BATCH_LISTENER
    )
    public void demoListener(
            List<ConsumerRecord<String, String>> messages,
            Acknowledgment acknowledgment
    ) {
        for (ConsumerRecord<String, String> message : messages) {
            EventEnvelop<EventPayload> envelop = EventEnvelop.fromJson(message.value());
            metricFacade.handle(envelop);
        }

        acknowledgment.acknowledge(); // manual ack
    }
}
