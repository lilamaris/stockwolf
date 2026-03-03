package com.lilamaris.stockwolf.event;

import com.lilamaris.stockwolf.event.core.factory.*;
import com.lilamaris.stockwolf.event.core.inbound.*;
import com.lilamaris.stockwolf.event.core.outbound.*;
import com.lilamaris.stockwolf.event.core.provider.*;
import com.lilamaris.stockwolf.event.core.serializer.EventCodec;
import com.lilamaris.stockwolf.event.core.serializer.JacksonEventCodec;
import com.lilamaris.stockwolf.event.core.serializer.PayloadDeserializer;
import com.lilamaris.stockwolf.event.core.serializer.PayloadSerializer;
import com.lilamaris.stockwolf.event.core.store.EventStore;
import com.lilamaris.stockwolf.event.core.store.SimpleEventStore;
import com.lilamaris.stockwolf.event.core.store.SimpleStoredEventEnvelopeFactory;
import com.lilamaris.stockwolf.event.core.store.StoredEventEnvelopeFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import tools.jackson.databind.ObjectMapper;

import java.time.Clock;
import java.util.List;

@AutoConfiguration
@AutoConfigureOrder(Ordered.LOWEST_PRECEDENCE)
public class EventAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(InboundRelay.class)
    InboundRelay inboundRelay(
            EventListenerRegistrar eventListenerRegistrar,
            PayloadDeserializer payloadDeserializer,
            EventStore eventStore
    ) {
        return new SimpleInboundRelay(
                eventListenerRegistrar,
                payloadDeserializer,
                eventStore
        );
    }

    @Bean
    @ConditionalOnMissingBean(OutboundRelay.class)
    OutboundRelay outboundRelay(
            EventStore eventStore,
            EventSender eventSender
    ) {
        return new SimpleOutboundRelay(
                eventStore,
                eventSender
        );
    }

    @Bean
    @ConditionalOnMissingBean(EventPublisher.class)
    EventPublisher eventPublisher(
            EventHeaderFactory eventHeaderFactory,
            EventTraceFactory eventTraceFactory,
            EventEnvelopeFactory eventEnvelopeFactory,
            EventStore eventStore,
            PayloadSerializer payloadSerializer
    ) {
        return new SimpleEventPublisher(
                eventHeaderFactory,
                eventTraceFactory,
                eventEnvelopeFactory,
                eventStore,
                payloadSerializer
        );
    }

    @Bean
    @ConditionalOnMissingBean(EventStore.class)
    EventStore eventStore(
            StoredEventEnvelopeFactory storedEventEnvelopeFactory
    ) {
        return new SimpleEventStore(
                storedEventEnvelopeFactory
        );
    }

    @Bean
    @ConditionalOnMissingBean(EventSender.class)
    EventSender eventSender() {
        return new LogOnlyEventSender();
    }

    @Bean
    @ConditionalOnMissingBean(EventListenerRegistrar.class)
    EventListenerRegistrar eventListenerRegistrar(
            List<EventListener<?>> listeners
    ) {
        return new SimpleEventListenerRegistrar(
                listeners
        );
    }

    @Bean
    @ConditionalOnMissingBean(EventCodec.class)
    EventCodec eventCodec(
            ObjectMapper objectMapper
    ) {
        return new JacksonEventCodec(objectMapper);
    }

    @Bean
    @ConditionalOnMissingBean(PayloadSerializer.class)
    PayloadSerializer payloadSerializer(
            ObjectMapper objectMapper
    ) {
        return new JacksonEventCodec(objectMapper);
    }

    @Bean
    @ConditionalOnMissingBean(PayloadDeserializer.class)
    PayloadDeserializer payloadDeserializer(
            ObjectMapper objectMapper
    ) {
        return new JacksonEventCodec(objectMapper);
    }

    @Bean
    @ConditionalOnMissingBean(EventHeaderFactory.class)
    EventHeaderFactory eventHeaderFactory(
            Clock clock
    ) {
        return new SimpleEventHeaderFactory(clock);
    }

    @Bean
    @ConditionalOnMissingBean(EventTraceFactory.class)
    EventTraceFactory eventTraceFactory(
            EventIdProvider eventIdProvider,
            ProducerProvider producerProvider,
            CorrelationProvider correlationProvider
    ) {
        return new SimpleEventTraceFactory(
                eventIdProvider,
                producerProvider,
                correlationProvider
        );
    }

    @Bean
    @ConditionalOnMissingBean(EventDynamicContextFactory.class)
    EventDynamicContextFactory eventDynamicContextFactory() {
        return new SimpleEventDynamicContextFactory();
    }

    @Bean
    @ConditionalOnMissingBean(EventEnvelopeFactory.class)
    EventEnvelopeFactory eventEnvelopeFactory() {
        return new SimpleEventEnvelopeFactory();
    }

    @Bean
    @ConditionalOnMissingBean(StoredEventEnvelopeFactory.class)
    StoredEventEnvelopeFactory storedEventEnvelopeFactory() {
        return new SimpleStoredEventEnvelopeFactory();
    }

    @Bean
    @ConditionalOnMissingBean(Clock.class)
    Clock clock() {
        return Clock.systemUTC();
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
}
