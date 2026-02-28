package com.lilamaris.stockwolf.event.support.jpa;

import com.lilamaris.stockwolf.event.foundation.relay.OutboundRelay;
import org.springframework.scheduling.annotation.Scheduled;

public class JpaOutboundStoreScheduler {
    private final OutboundRelay outboundRelay;

    public JpaOutboundStoreScheduler(
            OutboundRelay outboundRelay
    ) {
        this.outboundRelay = outboundRelay;
    }

    @Scheduled(fixedDelayString = "${event.outbound.relay.fixedDelayMs:500}")
    public void tick() {
        outboundRelay.batch(100);
    }
}
