package com.lilamaris.stockwolf.event.core.scheduler;

import com.lilamaris.stockwolf.event.core.outbound.OutboundRelay;
import org.springframework.scheduling.annotation.Scheduled;

public class FixedDelayEventScheduler implements EventScheduler {
    private final OutboundRelay outboundRelay;
    private final int batchSize;

    public FixedDelayEventScheduler(
            OutboundRelay outboundRelay,
            int batchSize
    ) {
        this.outboundRelay = outboundRelay;
        this.batchSize = batchSize;
    }

    @Scheduled(fixedDelayString = "${event.scheduler.delayMs:500}")
    public void tick() {
        outboundRelay.batch(batchSize);
    }
}
