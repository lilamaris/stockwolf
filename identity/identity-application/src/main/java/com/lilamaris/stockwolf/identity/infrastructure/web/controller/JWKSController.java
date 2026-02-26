package com.lilamaris.stockwolf.identity.infrastructure.web.controller;

import com.lilamaris.stockwolf.identity.application.port.in.JWKSReader;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class JWKSController {
    private final JWKSReader jwksReader;

    @GetMapping("/.well-known/jwks.json")
    public Map<String, Object> keys() {
        return jwksReader.read().toPublicJWKSet().toJSONObject();
    }
}
