package com.lilamaris.stockwolf.event.foundation;

import com.lilamaris.stockwolf.event.core.EventKey;

public record DefaultEventKey(
        String name
) implements EventKey {
    public static EventKey of(String name) {
        return new DefaultEventKey(name);
    }
}
