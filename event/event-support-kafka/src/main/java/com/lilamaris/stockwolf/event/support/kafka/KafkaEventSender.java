package com.lilamaris.stockwolf.event.support.kafka;

import com.lilamaris.stockwolf.event.core.EventEnvelope;
import com.lilamaris.stockwolf.event.core.outbound.EventDefinitionRegistrar;
import com.lilamaris.stockwolf.event.core.outbound.EventSender;
import com.lilamaris.stockwolf.event.core.serializer.EventSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;

public class KafkaEventSender implements EventSender {
    private static final Logger log = LoggerFactory.getLogger(KafkaEventSender.class);
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final EventDefinitionRegistrar eventDefinitionRegistrar;
    private final EventSerializer eventSerializer;
    private final TopicFactory topicFactory;

    public KafkaEventSender(
            KafkaTemplate<String, String> kafkaTemplate,
            EventDefinitionRegistrar eventDefinitionRegistrar,
            EventSerializer eventSerializer,
            TopicFactory topicFactory
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.eventDefinitionRegistrar = eventDefinitionRegistrar;
        this.eventSerializer = eventSerializer;
        this.topicFactory = topicFactory;
    }

    @Override
    public void send(EventEnvelope eventEnvelope) {
        var eventKey = eventEnvelope.header().eventKey();
        var eventDefinition = eventDefinitionRegistrar.resolve(eventKey);
        var topic = topicFactory.build(eventDefinition);
        var key = eventEnvelope.trace().eventId();
        log.debug("""
                Send payload={}""", eventEnvelope);
        var data = eventSerializer.stringify(eventEnvelope);
        kafkaTemplate.send(topic, key, data);

        log.debug("""
                Kafka send message
                topic={}
                key={},
                data={}""", topic, key, data);
    }
}
