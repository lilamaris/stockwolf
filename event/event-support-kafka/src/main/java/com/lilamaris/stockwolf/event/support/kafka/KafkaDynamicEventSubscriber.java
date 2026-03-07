package com.lilamaris.stockwolf.event.support.kafka;

import com.lilamaris.stockwolf.event.core.inbound.EventListener;
import com.lilamaris.stockwolf.event.core.inbound.EventListenerRegistrar;
import com.lilamaris.stockwolf.event.core.outbound.EventDefinitionRegistrar;
import com.lilamaris.stockwolf.event.core.provider.ProducerProvider;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.config.MethodKafkaListenerEndpoint;
import org.springframework.messaging.handler.annotation.support.MessageHandlerMethodFactory;

import java.util.Objects;

public class KafkaDynamicEventSubscriber {
    private static final Logger log = LoggerFactory.getLogger(KafkaDynamicEventSubscriber.class);
    private final EventListenerRegistrar eventListenerRegistrar;
    private final EventDefinitionRegistrar eventDefinitionRegistrar;
    private final ProducerProvider producerProvider;
    private final TopicFactory topicFactory;
    private final KafkaEndpointHandler kafkaEndpointHandler;

    private final MessageHandlerMethodFactory messageHandlerMethodFactory;
    private final KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;
    private final ConcurrentKafkaListenerContainerFactory<String, byte[]> concurrentKafkaListenerContainerFactory;

    public KafkaDynamicEventSubscriber(
            EventListenerRegistrar eventListenerRegistrar,
            EventDefinitionRegistrar eventDefinitionRegistrar,
            ProducerProvider producerProvider,
            TopicFactory topicFactory,
            KafkaEndpointHandler kafkaEndpointHandler,
            MessageHandlerMethodFactory messageHandlerMethodFactory,
            KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry,
            ConcurrentKafkaListenerContainerFactory<String, byte[]> concurrentKafkaListenerContainerFactory
    ) {
        this.eventListenerRegistrar = eventListenerRegistrar;
        this.eventDefinitionRegistrar = eventDefinitionRegistrar;
        this.producerProvider = producerProvider;
        this.topicFactory = topicFactory;
        this.kafkaEndpointHandler = kafkaEndpointHandler;
        this.messageHandlerMethodFactory = messageHandlerMethodFactory;
        this.kafkaListenerEndpointRegistry = kafkaListenerEndpointRegistry;
        this.concurrentKafkaListenerContainerFactory = concurrentKafkaListenerContainerFactory;
    }

    @org.springframework.context.event.EventListener(ApplicationReadyEvent.class)
    public void runAfterBoot() throws NoSuchMethodException {
        var eventKeys = eventListenerRegistrar.listAll().stream()
                .map(EventListener::key)
                .toList();
        var candidateTopics = eventKeys.stream()
                .map(eventDefinitionRegistrar::resolve)
                .filter(Objects::nonNull)
                .map(topicFactory::build)
                .distinct()
                .toList();

        log.debug("""
                Event subscription candidate topics={}""", candidateTopics);

        if (candidateTopics.isEmpty()) {
            log.debug("""
                    None of topics that request subscribe. Endpoint register progress stopped.""");
            return;
        }

        var topics = candidateTopics.toArray(String[]::new);
        var endpoint = new MethodKafkaListenerEndpoint<String, byte[]>();
        endpoint.setId("event-dynamic-subscriber");
        endpoint.setGroupId(producerProvider.producer());
        endpoint.setTopics(topics);

        endpoint.setBean(kafkaEndpointHandler);
        endpoint.setMessageHandlerMethodFactory(messageHandlerMethodFactory);
        endpoint.setMethod(KafkaEndpointHandler.class.getMethod(
                "onMessage", ConsumerRecord.class
        ));

        kafkaListenerEndpointRegistry.registerListenerContainer(endpoint, concurrentKafkaListenerContainerFactory, true);
        var container = kafkaListenerEndpointRegistry.getListenerContainer("event-dynamic-subscriber");
        log.debug("container exists? {}, running? {}",
                container != null,
                container != null && container.isRunning());
    }
}
