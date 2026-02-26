package com.lilamaris.stockwolf.identity.infrastructure.security.authn.credential.token;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.List;

@Getter
public class CredentialSignInAuthentication extends AbstractAuthenticationToken {
    private final String email;
    private String rawPassword;

    public CredentialSignInAuthentication(String email, String rawPassword) {
        super(List.of());
        setAuthenticated(false);
        this.email = email;
        this.rawPassword = rawPassword;
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.rawPassword = null;
    }

    @Override
    public Object getPrincipal() {
        return email;
    }

    @Override
    public Object getCredentials() {
        return rawPassword;
    }
}
