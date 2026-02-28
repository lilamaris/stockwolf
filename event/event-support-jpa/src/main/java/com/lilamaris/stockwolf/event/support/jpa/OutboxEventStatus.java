package com.lilamaris.stockwolf.event.support.jpa;

public enum OutboxEventStatus {
    PENDING,
    SENT,
    FAILED
}
