package com.lilamaris.stockwolf.order.application.port.in;

import com.lilamaris.stockwolf.order.domain.Order;
import com.lilamaris.stockwolf.order.domain.OrderStatus;

public record OrderEntry(
        String correlationId,
        String userId,
        OrderStatus status
) {
    public static OrderEntry from(Order order) {
        return new OrderEntry(order.getCorrelationId(), order.getUserId(), order.getStatus());
    }
}
