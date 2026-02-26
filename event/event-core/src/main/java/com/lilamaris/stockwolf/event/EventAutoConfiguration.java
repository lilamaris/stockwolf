package com.lilamaris.stockwolf.event;

import com.lilamaris.stockwolf.event.core.EventDefinition;
import com.lilamaris.stockwolf.event.core.EventHandler;
import com.lilamaris.stockwolf.event.core.serializer.EventCodec;
import com.lilamaris.stockwolf.event.foundation.EventRegistry;
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
            List<EventHandler<?>> eventHandlers,
            EventCodec codec
    ) {
        return new EventRegistry(eventDefinitions, eventHandlers, codec);
    }
}
