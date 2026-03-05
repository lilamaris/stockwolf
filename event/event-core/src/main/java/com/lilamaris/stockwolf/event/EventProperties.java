package com.lilamaris.stockwolf.event;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "event")
public record EventProperties(
        SchedulerProperties scheduler
) {
    public record SchedulerProperties(
            int batchSize
    ) {
    }
}

