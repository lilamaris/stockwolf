package com.lilamaris.stockwolf.identity.application.port.in;

import com.nimbusds.jose.jwk.JWKSet;

public interface JWKSReader {
    JWKSet read();
}
