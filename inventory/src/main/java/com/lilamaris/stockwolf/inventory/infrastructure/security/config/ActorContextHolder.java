package com.lilamaris.stockwolf.inventory.infrastructure.security.config;

import com.lilamaris.stockwolf.inventory.application.exception.InventoryErrorCode;
import com.lilamaris.stockwolf.inventory.application.exception.InventoryInvariantException;
import com.lilamaris.stockwolf.inventory.application.port.out.Actor;
import com.lilamaris.stockwolf.inventory.application.port.out.ActorContext;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ActorContextHolder implements ActorContext {
    @Override
    public Actor get() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            throw new InventoryInvariantException(InventoryErrorCode.UNIDENTIFIED_ACTOR);
        }
        String principal = Optional.ofNullable(auth.getPrincipal())
                .map(Object::toString)
                .orElseThrow(() -> new InventoryInvariantException(InventoryErrorCode.UNIDENTIFIED_ACTOR));

        Set<String> authorities = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(Objects::nonNull)
                .collect(Collectors.toUnmodifiableSet());

        return new Actor(principal, authorities);
    }
}
