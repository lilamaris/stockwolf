package com.lilamaris.stockwolf.identity.application.port.in;

public interface UserRegister {
    PrincipalEntry register(String email, String name, String rawPassword);
}
