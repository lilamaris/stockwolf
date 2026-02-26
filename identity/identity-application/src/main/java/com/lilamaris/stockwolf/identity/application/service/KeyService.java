package com.lilamaris.stockwolf.identity.application.service;

import com.lilamaris.stockwolf.identity.application.port.in.JWKSReader;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KeyService implements JWKSReader {
    private final RSAKey rsaKey;

    @Override
    public JWKSet read() {
        return new JWKSet(rsaKey);
    }
}
