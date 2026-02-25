package com.lilamaris.stockwolf.event.supports.kafka;

import com.lilamaris.stockwolf.event.foundation.EventDefinitionRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ObjectMapper;

@Configuration
public class EventHandlerKafkaAutoConfiguration {
    @Bean
    EventCodec eventCodec(
            ObjectMapper objectMapper,
            EventDefinitionRegistry registry
    ) {
        return new JacksonEventCodec(objectMapper, registry);
    }

    @Bean
    EventDispatcher eventDispatcher(
            EventCodec codec,
            EventHandlerRegistry registry
    ) {
        return new DefaultEventDispatcher(codec, registry);
    }
}
