package com.lilamaris.stockwolf.order.application.service;

import com.lilamaris.stockwolf.event.core.factory.EventDynamicContextFactory;
import com.lilamaris.stockwolf.event.core.outbound.EventPublisher;
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
    private final EventDynamicContextFactory eventDynamicContextFactory;
    private final EventPublisher eventPublisher;

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

        var eventDynamicContext = eventDynamicContextFactory.build(
                "order",
                order.getId().toString(),
                null,
                null
        );
        eventPublisher.publish(
                OrderEventKey.ORDER_CREATED,
                eventDynamicContext,
                new OrderCreatedEventPayload(order.getId().toString())
        );

        return OrderEntry.from(order);
    }
}
