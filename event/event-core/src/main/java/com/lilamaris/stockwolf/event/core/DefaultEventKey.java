package com.lilamaris.stockwolf.event.core;

public record DefaultEventKey(
        String name
) implements EventKey {
    public static EventKey of(String name) {
        return new DefaultEventKey(name);
    }
}
