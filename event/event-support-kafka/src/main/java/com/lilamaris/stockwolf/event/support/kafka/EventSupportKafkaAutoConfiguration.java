package com.lilamaris.stockwolf.event.support.kafka;

import com.lilamaris.stockwolf.event.core.inbound.EventListenerRegistrar;
import com.lilamaris.stockwolf.event.core.outbound.EventDefinitionRegistrar;
import com.lilamaris.stockwolf.event.core.outbound.EventSender;
import com.lilamaris.stockwolf.event.core.provider.ProducerProvider;
import com.lilamaris.stockwolf.event.core.serializer.EventCodec;
import com.lilamaris.stockwolf.event.core.serializer.EventSerializer;
import com.lilamaris.stockwolf.event.core.store.EventStore;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import org.springframework.messaging.handler.annotation.support.MessageHandlerMethodFactory;

@AutoConfiguration
@ConditionalOnClass(KafkaTemplate.class)
@EnableKafka
@EnableConfigurationProperties(EventSupportKafkaProperties.class)
public class EventSupportKafkaAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(EventSender.class)
    EventSender eventSender(
            KafkaTemplate<String, String> kafkaTemplate,
            EventDefinitionRegistrar eventDefinitionRegistrar,
            EventSerializer eventSerializer,
            TopicFactory topicFactory
    ) {
        return new KafkaEventSender(
                kafkaTemplate,
                eventDefinitionRegistrar,
                eventSerializer,
                topicFactory
        );
    }

    @Bean
    @ConditionalOnMissingBean(TopicFactory.class)
    TopicFactory topicFactory(
            EventSupportKafkaProperties properties
    ) {
        return new DefinitionBasedTopicFactory(properties.topic());
    }

    @Bean
    MessageHandlerMethodFactory messageHandlerMethodFactory() {
        return new DefaultMessageHandlerMethodFactory();
    }

    @Bean
    KafkaEndpointHandler kafkaEndpointHandler(
            EventStore eventStore,
            EventCodec eventCodec
    ) {
        return new KafkaEndpointHandler(
                eventStore,
                eventCodec
        );
    }

    @Bean
    KafkaDynamicEventSubscriber kafkaDynamicEventSubscriber(
            EventListenerRegistrar eventListenerRegistrar,
            EventDefinitionRegistrar eventDefinitionRegistrar,
            ProducerProvider producerProvider,
            TopicFactory topicFactory,
            KafkaEndpointHandler kafkaEndpointHandler,
            MessageHandlerMethodFactory messageHandlerMethodFactory,
            KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry,
            ConcurrentKafkaListenerContainerFactory<String, byte[]> concurrentKafkaListenerContainerFactory
    ) {
        return new KafkaDynamicEventSubscriber(
                eventListenerRegistrar,
                eventDefinitionRegistrar,
                producerProvider,
                topicFactory,
                kafkaEndpointHandler,
                messageHandlerMethodFactory,
                kafkaListenerEndpointRegistry,
                concurrentKafkaListenerContainerFactory
        );
    }
}
