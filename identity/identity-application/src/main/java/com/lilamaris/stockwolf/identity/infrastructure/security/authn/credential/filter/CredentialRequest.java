package com.lilamaris.stockwolf.identity.infrastructure.security.authn.credential.filter;

public class CredentialRequest {
    public record SignIn(String email, String password) {
    }

    public record Register(String displayName, String email, String password) {
    }
}