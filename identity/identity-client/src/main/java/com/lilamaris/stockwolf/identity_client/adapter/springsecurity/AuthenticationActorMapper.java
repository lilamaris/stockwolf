package com.lilamaris.stockwolf.identity_client.adapter.springsecurity;

import com.lilamaris.stockwolf.identity_client.core.Actor;
import com.lilamaris.stockwolf.identity_client.core.exception.UnidentifiableActorException;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor
public class AuthenticationActorMapper {
    public static Actor from(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new UnidentifiableActorException("Authenticated actor not found");
        }

        String subject = authentication.getPrincipal().toString();
        Set<String> scopes = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(Objects::nonNull)
                .collect(Collectors.toUnmodifiableSet());

        return new Actor(subject, scopes);
    }
}
