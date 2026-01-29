package com.lilamaris.stockwolf.inventory.application.exception;

public class ApplicationAuthenticationException extends DefaultApplicationException {
    public ApplicationAuthenticationException(ApplicationErrorCode errorCode) {
        super(errorCode);
    }
}
