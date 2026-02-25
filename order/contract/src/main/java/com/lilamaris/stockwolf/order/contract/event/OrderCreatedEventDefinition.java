package com.lilamaris.stockwolf.order.contract.event;

import com.lilamaris.stockwolf.event.core.EventDefinition;
import com.lilamaris.stockwolf.event.core.payload.EventEnvelope;

public class OrderCreatedEventDefinition implements EventDefinition<OrderCreatedEventPayload> {
    @Override
    public String key() {
        return "order.created";
    }

    @Override
    public Class<OrderCreatedEventPayload> payload() {
        return OrderCreatedEventPayload.class;
    }

    @Override
    public void handle(OrderCreatedEventPayload payload) throws Exception {

    }
}
