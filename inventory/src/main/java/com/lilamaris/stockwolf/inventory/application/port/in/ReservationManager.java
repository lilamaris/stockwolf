package com.lilamaris.stockwolf.inventory.application.port.in;

public interface ReservationManager {
    ReservationEntry reserve(
            String correlationId,
            String skuId,
            int quantity
    );
}
