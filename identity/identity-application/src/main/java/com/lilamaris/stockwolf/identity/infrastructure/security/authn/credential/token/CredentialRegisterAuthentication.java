package com.lilamaris.stockwolf.identity.infrastructure.security.authn.credential.token;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.List;

@Getter
public class CredentialRegisterAuthentication extends AbstractAuthenticationToken {
    private final String email;
    private final String displayName;
    private String rawPassword;

    public CredentialRegisterAuthentication(String email, String rawPassword, String displayName) {
        super(List.of());
        setAuthenticated(false);

        this.email = email;
        this.rawPassword = rawPassword;
        this.displayName = displayName;
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        this.rawPassword = null;
    }

    @Override
    public Object getCredentials() {
        return rawPassword;
    }

    @Override
    public Object getPrincipal() {
        return email;
    }
}
