package com.lilamaris.stockwolf.event.support.kafka;

import com.lilamaris.stockwolf.event.core.EventKey;
import com.lilamaris.stockwolf.event.core.relay.outbound.EventPublisher;
import org.springframework.kafka.core.KafkaTemplate;

public class KafkaEventPublisher implements EventPublisher {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaEventPublisher(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publish(String topic, EventKey eventKey, String raw) {
        kafkaTemplate.send(topic, eventKey.name(), raw);
    }
}
