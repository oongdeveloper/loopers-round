package com.loopers.application.metric;

import com.loopers.config.kafka.KafkaConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductMetricConsumer {
    @KafkaListener(
            topics = {"catalog-events","order-events"},
            containerFactory = KafkaConfig.BATCH_LISTENER
    )
    public void demoListener(
            List<ConsumerRecord<Object, Object>> messages,
            Acknowledgment acknowledgment
    ) {
        System.out.println(messages);
        acknowledgment.acknowledge(); // manual ack
    }
}
