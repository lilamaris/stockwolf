package com.lilamaris.stockwolf.inventory.util;

import java.util.concurrent.atomic.AtomicInteger;

public class SequentialIdentifyGenerator {
    private final AtomicInteger counter = new AtomicInteger();
    private final String prefix;

    public SequentialIdentifyGenerator(String prefix) {
        this.prefix = prefix;
    }

    public String generate() {
        int count = counter.getAndIncrement();
        return prefix + ":" + count;
    }
}
