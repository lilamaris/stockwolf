package com.lilamaris.stockwolf.event;

import com.lilamaris.stockwolf.event.core.EventDefinition;
import com.lilamaris.stockwolf.event.foundation.EventDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.List;

@AutoConfiguration
public class EventAutoConfiguration {
    @Bean
    EventDefinitionRegistry eventDefinitionRegistry(List<EventDefinition<?>> eventDefinitions) {
        return new EventDefinitionRegistry(eventDefinitions);
    }
}
