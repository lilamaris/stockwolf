package com.lilamaris.stockwolf.event.core.outbound;

import com.lilamaris.stockwolf.event.core.EventKey;
import com.lilamaris.stockwolf.event.core.EventPayload;

public interface EventDefinition<P extends EventPayload> {
    EventKey key();

    String producer();

    Class<P> payload();
}
