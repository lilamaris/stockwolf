package com.lilamaris.stockwolf.event.supports.jpa;

public enum OutboxEventStatus {
    PENDING,
    SENT,
    FAILED
}
