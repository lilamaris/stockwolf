package com.lilamaris.stockwolf.identity.infrastructure.security.authn.credential.token;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.List;

@Getter
public class RefreshTokenAuthentication extends AbstractAuthenticationToken {
    private String refreshToken;

    public RefreshTokenAuthentication(String refreshToken) {
        super(List.of());
        setAuthenticated(false);

        this.refreshToken = refreshToken;
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.refreshToken = null;
    }

    @Override
    public Object getCredentials() {
        return refreshToken;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }
}
