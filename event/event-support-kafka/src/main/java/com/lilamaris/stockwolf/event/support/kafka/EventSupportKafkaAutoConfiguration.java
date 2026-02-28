package com.lilamaris.stockwolf.event.support.kafka;

import com.lilamaris.stockwolf.event.core.relay.outbound.EventPublisher;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

@AutoConfiguration
@ConditionalOnClass(KafkaTemplate.class)
public class EventSupportKafkaAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(EventPublisher.class)
    EventPublisher eventPublisher(
            KafkaTemplate<String, String> kafkaTemplate
    ) {
        return new KafkaEventPublisher(kafkaTemplate);
    }
}
