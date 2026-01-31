package com.lilamaris.stockwolf.identity_client.adapter.springsecurity;

import com.lilamaris.stockwolf.identity_client.core.Introspector;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;

@RequiredArgsConstructor
public class IntrospectionAuthenticationProvider implements AuthenticationProvider {
    private final Introspector introspector;

    @Override
    public @Nullable Authentication authenticate(@NonNull Authentication authentication) throws AuthenticationException {
        if (!(authentication instanceof BearerTokenAuthentication bearer)) {
            return null;
        }

        String token = (String) bearer.getCredentials();
        var introspection = introspector.introspect(token);

        if (!introspection.active()) {
            throw new BadCredentialsException("Inactive token");
        }

        var authorities = Arrays.stream(introspection.scope().split(" "))
                .map(s -> new SimpleGrantedAuthority("SCOPE_" + s))
                .toList();

        return new BearerTokenAuthentication(
                token,
                introspection,
                authorities
        );
    }

    @Override
    public boolean supports(@NonNull Class<?> authentication) {
        return BearerTokenAuthentication.class.isAssignableFrom(authentication);
    }
}
