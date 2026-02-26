package com.lilamaris.stockwolf.identity.application.exception;

public class IdentityIllegalStateException extends IdentityApplicationException {
    public IdentityIllegalStateException(IdentityErrorCode errorCode) {
        super(errorCode);
    }
}
