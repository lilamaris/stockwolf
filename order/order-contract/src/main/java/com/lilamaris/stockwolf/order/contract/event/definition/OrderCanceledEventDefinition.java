package com.lilamaris.stockwolf.order.contract.event.definition;

import com.lilamaris.stockwolf.event.core.EventDefinition;
import com.lilamaris.stockwolf.event.core.EventKey;
import com.lilamaris.stockwolf.order.contract.event.payload.OrderCanceledEventPayload;

public class OrderCanceledEventDefinition implements EventDefinition<OrderCanceledEventPayload> {
    @Override
    public EventKey key() {
        return OrderEventKey.ORDER_CANCELED;
    }

    @Override
    public String producer() {
        return "order-service";
    }

    @Override
    public Class<OrderCanceledEventPayload> payload() {
        return OrderCanceledEventPayload.class;
    }
}
