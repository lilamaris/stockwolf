package com.lilamaris.stockwolf.event.core.outbound;

import com.lilamaris.stockwolf.event.core.EventDynamicContext;
import com.lilamaris.stockwolf.event.core.EventKey;
import com.lilamaris.stockwolf.event.core.EventPayload;

public interface EventPublisher {
    <P extends EventPayload> void publish(
            EventKey eventKey,
            EventDynamicContext eventDynamicContext,
            P eventPayload
    );
}