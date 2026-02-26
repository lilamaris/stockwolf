package com.lilamaris.stockwolf.identity_client.core;

public interface Introspection {
    Boolean active();

    String subject();

    String scope();

    Long exp();
}
