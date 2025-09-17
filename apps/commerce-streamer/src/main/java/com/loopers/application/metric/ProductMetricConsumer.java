package com.loopers.application.metric;

import com.loopers.config.kafka.KafkaConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class ProductMetricConsumer {
    private final ProductMetricFacade metricFacade;
    private final EventDuplicationService duplicationService;

    public ProductMetricConsumer(ProductMetricFacade metricFacade, EventDuplicationService duplicationService) {
        this.metricFacade = metricFacade;
        this.duplicationService = duplicationService;
    }

//    @KafkaListener(
//            topics = {"catalog-events","order-events"},
//            groupId = "product-metric-aggregate",
//            containerFactory = KafkaConfig.BATCH_LISTENER
//    )
//    public void consume(
//            List<ConsumerRecord<String, String>> messages,
//            Acknowledgment acknowledgment
//    ) {
//        for (ConsumerRecord<String, String> message : messages) {
//            EventEnvelop<EventPayload> envelop = EventEnvelop.fromJson(message.value());
//            metricFacade.handle(envelop);
//        }
//
//        acknowledgment.acknowledge(); // manual ack
//    }

    @KafkaListener(
            topics = {"catalog-events","order-events"},
            groupId = "product-metric-aggregate",
            containerFactory = KafkaConfig.BATCH_LISTENER
    )
    public void batchConsume(
            List<ConsumerRecord<String, byte[]>> messages,
            Acknowledgment acknowledgment
    ) {
        List<AggregateEvent> events = new ArrayList<>();
        for (ConsumerRecord<String, byte[]> message : messages) {
            try{
                AggregateEvent event = duplicationService.validAndConvert(message);
                if (event != null)
                    events.add(event);
            } catch (RuntimeException e){
                log.error("Kafka Consume 시 메세지 파싱 오류 발생 {}", message.headers().lastHeader("eventId"));
            }
        }
        metricFacade.handle(events);
        acknowledgment.acknowledge();
    }
}
