package com.lilamaris.stockwolf.inventory.application.exception;

public class ApplicationInternalException extends ApplicationException {
    public ApplicationInternalException(ApplicationErrorCode errorCode) {
        super(errorCode);
    }
}
