package com.lilamaris.stockwolf.order.contract.event.payload;

import com.lilamaris.stockwolf.event.core.EventPayload;

public record OrderCanceledEventPayload(
        String orderId
) implements EventPayload {
}