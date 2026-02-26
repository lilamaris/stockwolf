package com.lilamaris.stockwolf.order.domain;

public enum OrderStatus {
    CREATED,
    INVENTORY_RESERVED,
    INVENTORY_FAILED,
    PAYMENT_REQUESTED,
    PAID,
    PAYMENT_FAILED,
    CANCELLED
}
