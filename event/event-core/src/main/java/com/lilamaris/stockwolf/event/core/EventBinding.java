package com.lilamaris.stockwolf.event.core;

public interface EventBinding {
    EventKey key();

    void decodeAndHandle(String raw) throws Exception;
}
