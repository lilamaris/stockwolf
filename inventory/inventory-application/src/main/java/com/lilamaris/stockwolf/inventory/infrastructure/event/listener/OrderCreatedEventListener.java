package com.lilamaris.stockwolf.inventory.infrastructure.event.listener;

import com.lilamaris.stockwolf.event.core.EventHeader;
import com.lilamaris.stockwolf.event.core.EventKey;
import com.lilamaris.stockwolf.event.core.EventTrace;
import com.lilamaris.stockwolf.event.core.inbound.EventListener;
import com.lilamaris.stockwolf.order.contract.event.definition.OrderEventKey;
import com.lilamaris.stockwolf.order.contract.event.payload.OrderCreatedEventPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class OrderCreatedEventListener implements EventListener<OrderCreatedEventPayload> {
    @Override
    public EventKey key() {
        return OrderEventKey.ORDER_CREATED;
    }

    @Override
    public Class<OrderCreatedEventPayload> payload() {
        return OrderCreatedEventPayload.class;
    }

    public void handle(EventHeader header, EventTrace trace, OrderCreatedEventPayload payload) {
        log.info("this is {}, and i got message from {}, key is {}", "inventory-service", trace.producer(), header.eventKey());
    }
}
