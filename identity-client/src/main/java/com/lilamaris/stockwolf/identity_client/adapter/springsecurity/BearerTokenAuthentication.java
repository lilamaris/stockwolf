package com.lilamaris.stockwolf.identity_client.adapter.springsecurity;

import com.lilamaris.stockwolf.identity_client.core.Introspection;
import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

public class BearerTokenAuthentication extends AbstractAuthenticationToken {
    private final String token;
    private final Introspection introspection;

    public BearerTokenAuthentication(String token) {
        super(List.of());
        this.token = token;
        this.introspection = null;
        setAuthenticated(false);
    }

    public BearerTokenAuthentication(
            String token,
            Introspection introspection,
            Collection<? extends GrantedAuthority> authorities
    ) {
        super(authorities);
        this.token = token;
        this.introspection = introspection;
        setAuthenticated(true);
    }

    @Override
    public @Nullable Object getCredentials() {
        return token;
    }

    @Override
    public @Nullable Object getPrincipal() {
        return introspection;
    }
}
