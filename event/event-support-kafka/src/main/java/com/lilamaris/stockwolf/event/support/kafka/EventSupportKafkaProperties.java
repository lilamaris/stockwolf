package com.lilamaris.stockwolf.event.support.kafka;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "event.message")
public record EventSupportKafkaProperties(
        String topic
) {
}
