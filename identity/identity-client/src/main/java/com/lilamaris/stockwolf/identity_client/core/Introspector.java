package com.lilamaris.stockwolf.identity_client.core;

public interface Introspector {
    Introspection introspect(String token);
}
