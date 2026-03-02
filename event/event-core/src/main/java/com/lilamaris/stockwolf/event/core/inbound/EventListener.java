package com.lilamaris.stockwolf.event.core.inbound;

import com.lilamaris.stockwolf.event.core.EventHeader;
import com.lilamaris.stockwolf.event.core.EventKey;
import com.lilamaris.stockwolf.event.core.EventPayload;
import com.lilamaris.stockwolf.event.core.EventTrace;

public interface EventListener<P extends EventPayload> {
    EventKey key();

    Class<P> payload();

    void handle(
            EventHeader eventHeader,
            EventTrace eventTrace,
            P eventPayload
    );
}
