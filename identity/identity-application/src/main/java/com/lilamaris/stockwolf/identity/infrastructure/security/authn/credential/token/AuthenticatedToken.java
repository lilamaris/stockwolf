package com.lilamaris.stockwolf.identity.infrastructure.security.authn.credential.token;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.List;

@Getter
public class AuthenticatedToken extends AbstractAuthenticationToken {
    private final String accessToken;
    private final String refreshToken;

    public AuthenticatedToken(String accessToken, String refreshToken) {
        super(List.of());
        setAuthenticated(true);

        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    @Override
    public Object getCredentials() {
        return refreshToken;
    }

    @Override
    public Object getPrincipal() {
        return accessToken;
    }
}


