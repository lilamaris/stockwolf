package com.lilamaris.stockwolf.order.infrastructure.web.request;

import com.lilamaris.stockwolf.order.application.port.in.CreateOrderCommand;

import java.util.List;

public class OrderRequest {
    public record Create(
            List<CreateOrderCommand.OrderItemCommand> items
    ) {
    }
}
