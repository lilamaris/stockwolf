package com.lilamaris.stockwolf.identity.infrastructure.web.controller;

import com.lilamaris.stockwolf.identity.infrastructure.web.request.IntrospectionRequest;
import com.lilamaris.stockwolf.identity.infrastructure.web.response.IntrospectionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class IntrospectionController {
    private final JwtDecoder jwtDecoder;

    @PostMapping("/introspect")
    public ResponseEntity<IntrospectionResponse> introspect(
            @RequestBody IntrospectionRequest body
    ) {
        String token = body.token();

        try {
            Jwt jwt = jwtDecoder.decode(token);

            var result = IntrospectionResponse.active(
                    jwt.getSubject(),
                    jwt.getClaimAsString("scope"),
                    jwt.getExpiresAt() != null
                            ? jwt.getExpiresAt().getEpochSecond()
                            : null
            );

            return ResponseEntity.ok(result);
        } catch (JwtException e) {
            return ResponseEntity.ok(
                    IntrospectionResponse.inactive()
            );
        }
    }
}
