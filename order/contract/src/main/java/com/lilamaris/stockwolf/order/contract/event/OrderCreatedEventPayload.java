package com.lilamaris.stockwolf.order.contract.event;

import com.lilamaris.stockwolf.event.core.EventPayload;

public record OrderCreatedEventPayload(
        String orderId
) implements EventPayload {
}
