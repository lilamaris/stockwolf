package com.lilamaris.stockwolf.event.core.serializer;

import com.lilamaris.stockwolf.event.core.EventPayload;

public interface PayloadSerializer {
    <P extends EventPayload> String stringify(P payload);
}
