package com.lilamaris.stockwolf.event.supports.kafka;

public interface EventCodec {
    DecodedEvent decode(String raw);
}
