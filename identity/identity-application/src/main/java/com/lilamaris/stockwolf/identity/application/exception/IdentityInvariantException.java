package com.lilamaris.stockwolf.identity.application.exception;

public class IdentityInvariantException extends IdentityApplicationException {
    public IdentityInvariantException(IdentityErrorCode errorCode) {
        super(errorCode);
    }
}
