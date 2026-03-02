package com.lilamaris.stockwolf.event.core.inbound;

public interface InboundRelay {
    void batch(int size);
}
