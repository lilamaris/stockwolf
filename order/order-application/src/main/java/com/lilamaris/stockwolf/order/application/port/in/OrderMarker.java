package com.lilamaris.stockwolf.order.application.port.in;

public interface OrderMarker {
    OrderEntry markInventoryProcess(String correlationId, boolean success);

    OrderEntry markPaymentProcess(String correlationId, boolean success);
}
