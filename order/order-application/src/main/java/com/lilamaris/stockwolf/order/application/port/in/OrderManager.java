package com.lilamaris.stockwolf.order.application.port.in;

public interface OrderManager {
    OrderEntry create(CreateOrderCommand command);
}
