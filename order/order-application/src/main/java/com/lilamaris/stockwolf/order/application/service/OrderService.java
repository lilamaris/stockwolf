package com.lilamaris.stockwolf.order.application.service;

import com.lilamaris.stockwolf.order.application.port.in.OrderEntry;
import com.lilamaris.stockwolf.order.application.port.in.OrderManager;
import com.lilamaris.stockwolf.order.application.port.out.OrderStore;
import com.lilamaris.stockwolf.order.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService implements OrderManager {
    private final OrderStore orderStore;

    @Override
    public OrderEntry create(CreateOrderCommand command) {
        var correlationId = UUID.randomUUID().toString();

        var order = Order.create(
                correlationId,
                command.userId()
        );

        command.items().forEach(
                item -> order.addItem(
                        item.productId(),
                        item.quantity(),
                        item.price()
                ));

        orderStore.save(order);

        return OrderEntry.from(order);
    }
}
