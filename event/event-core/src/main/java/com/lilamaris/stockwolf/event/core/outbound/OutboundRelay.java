package com.lilamaris.stockwolf.event.core.outbound;

public interface OutboundRelay {
    void batch(int size);
}
