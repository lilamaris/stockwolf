package com.lilamaris.stockwolf.event.support.jpa;

import com.lilamaris.stockwolf.event.core.outbound.SimpleOutboundRelay;
import org.springframework.scheduling.annotation.Scheduled;

public class JpaOutboundStoreScheduler {
    private final SimpleOutboundRelay simpleOutboundRelay;

    public JpaOutboundStoreScheduler(
            SimpleOutboundRelay simpleOutboundRelay
    ) {
        this.simpleOutboundRelay = simpleOutboundRelay;
    }

    @Scheduled(fixedDelayString = "${event.outbound.relay.fixedDelayMs:500}")
    public void tick() {
        simpleOutboundRelay.batch(100);
    }
}
