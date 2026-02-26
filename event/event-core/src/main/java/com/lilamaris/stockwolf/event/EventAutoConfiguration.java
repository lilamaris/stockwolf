package com.lilamaris.stockwolf.event;

import com.lilamaris.stockwolf.event.core.EventDefinition;
import com.lilamaris.stockwolf.event.core.relay.outgoing.EventHandler;
import com.lilamaris.stockwolf.event.core.relay.outgoing.EventPublisher;
import com.lilamaris.stockwolf.event.core.relay.outgoing.OutgoingStore;
import com.lilamaris.stockwolf.event.core.serializer.EventCodec;
import com.lilamaris.stockwolf.event.foundation.EventRegistry;
import com.lilamaris.stockwolf.event.relay.IngoingEventRelay;
import com.lilamaris.stockwolf.event.relay.OutgoingEventRelay;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import java.util.List;

@AutoConfiguration
public class EventAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(EventRegistry.class)
    EventRegistry eventDefinitionRegistry(
            List<EventDefinition<?>> eventDefinitions,
            List<EventHandler<?>> eventHandlers
    ) {
        return new EventRegistry(eventDefinitions, eventHandlers);
    }

    @Bean
    IngoingEventRelay ingoingEventRelay(
            EventRegistry registry,
            EventCodec codec
    ) {
        return new IngoingEventRelay(registry, codec);
    }

    @Bean
    OutgoingEventRelay outgoingEventRelay(
            OutgoingStore store,
            EventPublisher publisher,
            EventCodec codec
    ) {
        return new OutgoingEventRelay(store, publisher, codec);
    }

}
