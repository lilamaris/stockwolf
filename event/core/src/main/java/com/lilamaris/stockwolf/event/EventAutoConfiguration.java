package com.lilamaris.stockwolf.event;

import com.lilamaris.stockwolf.event.core.registry.EventDefinition;
import com.lilamaris.stockwolf.event.core.registry.EventDefinitionRegistry;
import com.lilamaris.stockwolf.event.foundation.DefaultEventDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.List;

@AutoConfiguration
public class EventAutoConfiguration {

    @Bean
    EventDefinitionRegistry eventDefinitionRegistry(List<EventDefinition<?>> eventDefinitions) {
        return new DefaultEventDefinitionRegistry(eventDefinitions);
    }
}
