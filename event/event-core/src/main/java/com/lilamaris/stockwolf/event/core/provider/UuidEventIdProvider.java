package com.lilamaris.stockwolf.event.core.provider;

import java.util.UUID;

public class UuidEventIdProvider implements EventIdProvider {
    @Override
    public String newId() {
        return UUID.randomUUID().toString();
    }
}
