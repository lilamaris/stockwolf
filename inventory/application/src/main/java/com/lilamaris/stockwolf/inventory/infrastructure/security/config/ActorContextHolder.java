package com.lilamaris.stockwolf.inventory.infrastructure.security.config;

import com.lilamaris.stockwolf.identity_client.adapter.springsecurity.AuthenticationActorMapper;
import com.lilamaris.stockwolf.identity_client.core.Actor;
import com.lilamaris.stockwolf.inventory.application.port.out.ActorContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class ActorContextHolder implements ActorContext {
    @Override
    public Actor get() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return AuthenticationActorMapper.from(auth);
    }
}
