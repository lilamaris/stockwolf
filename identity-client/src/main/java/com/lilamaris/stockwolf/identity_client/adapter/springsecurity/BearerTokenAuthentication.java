package com.lilamaris.stockwolf.identity_client.adapter.springsecurity;

import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

public class BearerTokenAuthentication extends AbstractAuthenticationToken {
    private final String token;
    private final String subject;

    public BearerTokenAuthentication(String token) {
        super(List.of());
        this.token = token;
        this.subject = null;
        setAuthenticated(false);
    }

    public BearerTokenAuthentication(
            String token,
            String subject,
            Collection<? extends GrantedAuthority> authorities
    ) {
        super(authorities);
        this.token = token;
        this.subject = subject;
        setAuthenticated(true);
    }

    @Override
    public @Nullable Object getCredentials() {
        return token;
    }

    @Override
    public @Nullable Object getPrincipal() {
        return subject;
    }
}
