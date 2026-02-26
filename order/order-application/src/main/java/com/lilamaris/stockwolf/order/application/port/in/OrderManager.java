package com.lilamaris.stockwolf.order.application.port.in;

import java.util.List;

public interface OrderManager {
    OrderEntry create(CreateOrderCommand command);

    record CreateOrderCommand(
            String userId,
            List<OrderItemCommand> items
    ) {
    }

    record OrderItemCommand(
            String productId,
            int quantity,
            int price
    ) {
    }
}
