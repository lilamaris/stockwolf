package com.lilamaris.stockwolf.event;

import com.lilamaris.stockwolf.event.core.factory.*;
import com.lilamaris.stockwolf.event.core.inbound.*;
import com.lilamaris.stockwolf.event.core.outbound.*;
import com.lilamaris.stockwolf.event.core.provider.*;
import com.lilamaris.stockwolf.event.core.scheduler.EventScheduler;
import com.lilamaris.stockwolf.event.core.scheduler.FixedDelayEventScheduler;
import com.lilamaris.stockwolf.event.core.serializer.*;
import com.lilamaris.stockwolf.event.core.store.EventStore;
import com.lilamaris.stockwolf.event.core.store.SimpleEventStore;
import com.lilamaris.stockwolf.event.core.store.SimpleStoredEventEnvelopeFactory;
import com.lilamaris.stockwolf.event.core.store.StoredEventEnvelopeFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import tools.jackson.databind.ObjectMapper;

import java.time.Clock;
import java.util.List;

@AutoConfiguration
@AutoConfigureOrder(Ordered.LOWEST_PRECEDENCE)
@EnableConfigurationProperties(EventProperties.class)
public class EventAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(InboundRelay.class)
    InboundRelay inboundRelay(
            EventStore eventStore,
            EventRouter eventRouter
    ) {
        return new SimpleInboundRelay(
                eventStore,
                eventRouter
        );
    }

    @Bean
    @ConditionalOnMissingBean(OutboundRelay.class)
    OutboundRelay outboundRelay(
            EventStore eventStore,
            EventSender eventSender,
            EventEnvelopeFactory eventEnvelopeFactory
    ) {
        return new SimpleOutboundRelay(
                eventStore,
                eventSender,
                eventEnvelopeFactory
        );
    }

    @Bean
    @ConditionalOnMissingBean(EventScheduler.class)
    EventScheduler eventScheduler(
            OutboundRelay outboundRelay,
            InboundRelay inboundRelay,
            EventProperties properties
    ) {
        return new FixedDelayEventScheduler(
                outboundRelay,
                inboundRelay,
                properties.scheduler().batchSize()
        );
    }

    @Bean
    @ConditionalOnMissingBean(EventPublisher.class)
    EventPublisher eventPublisher(
            EventHeaderFactory eventHeaderFactory,
            EventTraceFactory eventTraceFactory,
            EventEnvelopeFactory eventEnvelopeFactory,
            EventStore eventStore,
            EventSerializer eventSerializer
    ) {
        return new SimpleEventPublisher(
                eventHeaderFactory,
                eventTraceFactory,
                eventEnvelopeFactory,
                eventStore,
                eventSerializer
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
    @ConditionalOnMissingBean(EventRouter.class)
    EventRouter eventRouter() {
        return new LogOnlyEventRouter();
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
    @ConditionalOnMissingBean(EventDefinitionRegistrar.class)
    EventDefinitionRegistrar eventDefinitionRegistrar(
            List<EventDefinition<?>> definitions
    ) {
        return new SimpleEventDefinitionRegistrar(
                definitions
        );
    }

    @Bean
    @ConditionalOnMissingBean(EventCodec.class)
    EventCodec eventCodec(
            ObjectMapper objectMapper,
            EventEnvelopeFactory eventEnvelopeFactory
    ) {
        return new JacksonEventCodec(
                objectMapper,
                eventEnvelopeFactory
        );
    }

    @Bean
    @ConditionalOnMissingBean(EventSerializer.class)
    EventSerializer eventSerializer(
            ObjectMapper objectMapper
    ) {
        return new JacksonEventSerializer(
                objectMapper
        );
    }

    @Bean
    @ConditionalOnMissingBean(EventDeserializer.class)
    EventDeserializer eventDeserializer(
            ObjectMapper objectMapper
    ) {
        return new JacksonEventDeserializer(
                objectMapper
        );
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
