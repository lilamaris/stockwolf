package com.lilamaris.stockwolf.event.foundation.provider;

import com.lilamaris.stockwolf.event.core.provider.EventIdProvider;

import java.util.UUID;

public class UuidEventIdProvider implements EventIdProvider {
    @Override
    public String newId() {
        return UUID.randomUUID().toString();
    }
}
