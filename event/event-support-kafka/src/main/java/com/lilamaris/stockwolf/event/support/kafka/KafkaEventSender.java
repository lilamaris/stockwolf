package com.lilamaris.stockwolf.event.support.kafka;

import com.lilamaris.stockwolf.event.core.EventEnvelope;
import com.lilamaris.stockwolf.event.core.outbound.EventSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;

public class KafkaEventSender implements EventSender {
    private static final Logger log = LoggerFactory.getLogger(KafkaEventSender.class);
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final TopicFactory topicFactory;

    public KafkaEventSender(
            KafkaTemplate<String, String> kafkaTemplate,
            TopicFactory topicFactory
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicFactory = topicFactory;
    }

    @Override
    public void send(EventEnvelope eventEnvelope) {
        var topic = topicFactory.build(eventEnvelope);
        var key = eventEnvelope.trace().eventId();
        var data = eventEnvelope.rawPayload();
        kafkaTemplate.send(topic, key, data);

        log.debug("""
                Kafka send message
                topic={}
                key={},
                data={}""", topic, key, data);
    }
}
