package com.lilamaris.stockwolf.inventory.application.exception;

public class ApplicationInternalException extends DefaultApplicationException {
    public ApplicationInternalException(ApplicationErrorCode errorCode) {
        super(errorCode);
    }
}
