package com.lilamaris.stockwolf.order.application.service;

import com.lilamaris.stockwolf.event.core.outbound.OutboundStore;
import com.lilamaris.stockwolf.event.core.DefaultEventContext;
import com.lilamaris.stockwolf.order.application.port.in.CreateOrderCommand;
import com.lilamaris.stockwolf.order.application.port.in.OrderEntry;
import com.lilamaris.stockwolf.order.application.port.in.OrderManager;
import com.lilamaris.stockwolf.order.application.port.out.ActorContext;
import com.lilamaris.stockwolf.order.application.port.out.OrderStore;
import com.lilamaris.stockwolf.order.contract.event.definition.OrderEventKey;
import com.lilamaris.stockwolf.order.contract.event.payload.OrderCreatedEventPayload;
import com.lilamaris.stockwolf.order.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService implements OrderManager {
    private final OrderStore orderStore;

    private final ActorContext actorContext;
    private final OutboundStore outboundStore;

    @Override
    public OrderEntry create(CreateOrderCommand command) {
        var correlationId = UUID.randomUUID().toString();
        var actor = actorContext.get();

        var order = Order.create(
                correlationId,
                actor.subject()
        );

        command.items().forEach(
                item -> order.addItem(
                        item.productId(),
                        item.quantity(),
                        item.price()
                ));

        orderStore.save(order);

        outboundStore.enqueue(
                OrderEventKey.ORDER_CREATED,
                new DefaultEventContext("order", order.getId().toString(), null, null),
                new OrderCreatedEventPayload(order.getId().toString())
        );

        return OrderEntry.from(order);
    }
}
