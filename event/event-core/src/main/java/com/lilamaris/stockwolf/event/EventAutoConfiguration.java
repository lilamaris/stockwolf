package com.lilamaris.stockwolf.event;

import com.lilamaris.stockwolf.event.core.EventDefinition;
import com.lilamaris.stockwolf.event.core.EventHandler;
import com.lilamaris.stockwolf.event.core.provider.CorrelationProvider;
import com.lilamaris.stockwolf.event.core.provider.EventIdProvider;
import com.lilamaris.stockwolf.event.core.provider.ProducerProvider;
import com.lilamaris.stockwolf.event.core.serializer.EventCodec;
import com.lilamaris.stockwolf.event.foundation.EventRegistry;
import com.lilamaris.stockwolf.event.foundation.provider.SpringAppNameProducerProvider;
import com.lilamaris.stockwolf.event.foundation.provider.ThreadLocalCorrelationProvider;
import com.lilamaris.stockwolf.event.foundation.provider.UuidEventIdProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import java.util.List;

@AutoConfiguration
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
