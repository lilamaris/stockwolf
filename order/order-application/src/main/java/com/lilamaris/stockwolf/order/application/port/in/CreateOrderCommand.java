package com.lilamaris.stockwolf.order.application.port.in;

import java.util.List;

public record CreateOrderCommand(
        List<OrderItemCommand> items
) {
    public record OrderItemCommand(
            String productId,
            int quantity,
            int price
    ) {
    }
}
