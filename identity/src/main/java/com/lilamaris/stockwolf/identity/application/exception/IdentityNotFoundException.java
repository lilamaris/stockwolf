package com.lilamaris.stockwolf.identity.application.exception;

public class IdentityNotFoundException extends IdentityApplicationException {
    public IdentityNotFoundException(IdentityErrorCode errorCode) {
        super(errorCode);
    }
}
