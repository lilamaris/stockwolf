package com.lilamaris.stockwolf.event.core;

public record SimpleEventKey(
        String name
) implements EventKey {
    public static SimpleEventKey of(EventKey eventKey) {
        return new SimpleEventKey(eventKey.name());
    }
}
