package com.lilamaris.stockwolf.event.core.factory;

import com.lilamaris.stockwolf.event.core.EventHeader;
import com.lilamaris.stockwolf.event.core.EventKey;
import com.lilamaris.stockwolf.event.core.SimpleEventHeader;

import java.time.Clock;
import java.time.Instant;

public class SimpleEventHeaderFactory implements EventHeaderFactory {
    private final Clock clock;

    public SimpleEventHeaderFactory(
            Clock clock
    ) {
        this.clock = clock;
    }

    @Override
    public EventHeader build(EventKey eventKey) {
        Instant occurredAt = clock.instant();
        return new SimpleEventHeader(eventKey, occurredAt);
    }
}
