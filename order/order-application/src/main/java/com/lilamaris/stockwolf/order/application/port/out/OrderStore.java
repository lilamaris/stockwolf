package com.lilamaris.stockwolf.order.application.port.out;

import com.lilamaris.stockwolf.order.domain.Order;

import java.util.Optional;

public interface OrderStore {
    Optional<Order> getByCorrelationId(String correlationId);

    Order save(Order order);
}
