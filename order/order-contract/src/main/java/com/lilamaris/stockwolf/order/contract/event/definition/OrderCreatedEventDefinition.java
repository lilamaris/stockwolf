package com.lilamaris.stockwolf.order.contract.event.definition;

import com.lilamaris.stockwolf.event.core.EventDefinition;
import com.lilamaris.stockwolf.event.core.EventKey;
import com.lilamaris.stockwolf.order.contract.event.payload.OrderCreatedEventPayload;

public class OrderCreatedEventDefinition implements EventDefinition<OrderCreatedEventPayload> {
    @Override
    public EventKey key() {
        return OrderEventKey.ORDER_CREATED;
    }

    @Override
    public Class<OrderCreatedEventPayload> payload() {
        return OrderCreatedEventPayload.class;
    }
}
