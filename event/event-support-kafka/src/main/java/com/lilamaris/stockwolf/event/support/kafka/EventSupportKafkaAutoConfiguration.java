package com.lilamaris.stockwolf.event.support.kafka;

import com.lilamaris.stockwolf.event.core.outbound.EventSender;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;

@AutoConfiguration
@ConditionalOnClass(KafkaTemplate.class)
@EnableKafka
@EnableConfigurationProperties(EventSupportKafkaProperties.class)
public class EventSupportKafkaAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(EventSender.class)
    EventSender eventSender(
            KafkaTemplate<String, String> kafkaTemplate,
            TopicFactory topicFactory
    ) {
        return new KafkaEventSender(
                kafkaTemplate,
                topicFactory
        );
    }

    @Bean
    @ConditionalOnMissingBean(TopicFactory.class)
    TopicFactory topicFactory(
            EventSupportKafkaProperties properties
    ) {
        return new ProducerBasedTopicFactory(properties.topic());
    }
}
