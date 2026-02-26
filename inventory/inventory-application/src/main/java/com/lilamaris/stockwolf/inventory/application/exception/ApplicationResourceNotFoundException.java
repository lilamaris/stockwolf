package com.lilamaris.stockwolf.inventory.application.exception;

public class ApplicationResourceNotFoundException extends ApplicationException {
    public ApplicationResourceNotFoundException(ApplicationErrorCode errorCode) {
        super(errorCode);
    }
}
