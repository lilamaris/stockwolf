package com.lilamaris.stockwolf.event.core.scheduler;

import com.lilamaris.stockwolf.event.core.inbound.InboundRelay;
import com.lilamaris.stockwolf.event.core.outbound.OutboundRelay;
import org.springframework.scheduling.annotation.Scheduled;

public class FixedDelayEventScheduler implements EventScheduler {
    private final OutboundRelay outboundRelay;
    private final InboundRelay inboundRelay;
    private final int batchSize;

    public FixedDelayEventScheduler(
            OutboundRelay outboundRelay,
            InboundRelay inboundRelay,
            int batchSize
    ) {
        this.outboundRelay = outboundRelay;
        this.inboundRelay = inboundRelay;
        this.batchSize = batchSize;
    }

    @Scheduled(fixedDelayString = "${event.scheduler.delayMs:500}")
    public void tick() {
        outboundRelay.batch(batchSize);
        inboundRelay.batch(batchSize);
    }
}
