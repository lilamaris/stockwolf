package com.lilamaris.stockwolf.event;

import com.lilamaris.stockwolf.event.core.inbound.EventListener;
import com.lilamaris.stockwolf.event.core.outbound.EventDefinition;
import com.lilamaris.stockwolf.event.core.outbound.EventPublisher;
import com.lilamaris.stockwolf.event.core.outbound.OutboundStore;
import com.lilamaris.stockwolf.event.core.outbound.SimpleOutboundRelay;
import com.lilamaris.stockwolf.event.core.provider.*;
import com.lilamaris.stockwolf.event.core.serializer.EventCodec;
import com.lilamaris.stockwolf.event.core.serializer.JacksonEventCodec;
import com.lilamaris.stockwolf.event.foundation.EventRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@AutoConfiguration
@AutoConfigureOrder(Ordered.LOWEST_PRECEDENCE)
public class EventAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(EventRegistry.class)
    EventRegistry eventDefinitionRegistry(
            List<EventDefinition<?>> eventDefinitions,
            List<EventHandler<?>> eventHandlers,
            EventCodec codec
    ) {
        return new EventRegistry(eventDefinitions, eventHandlers, codec);
    }

    @Bean
    @ConditionalOnMissingBean(EventCodec.class)
    EventCodec eventCodec(
            ObjectMapper objectMapper
    ) {
        return new JacksonEventCodec(objectMapper);
    }

    @Bean
    EventIdProvider eventIdProvider() {
        return new UuidEventIdProvider();
    }

    @Bean
    ProducerProvider producerProvider(Environment env) {
        return new SpringAppNameProducerProvider(env);
    }

    @Bean
    CorrelationProvider correlationProvider() {
        return new ThreadLocalCorrelationProvider();
    }

    @Bean
    OutboundRelay outboundRelay(
            OutboundStore outboundStore,
            EventPublisher eventPublisher
    ) {
        return new OutboundRelay(
                outboundStore,
                eventPublisher
        );
    }
}
