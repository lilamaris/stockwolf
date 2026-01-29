package com.lilamaris.stockwolf.inventory.application.exception;

public class ApplicationResourceNotFoundException extends DefaultApplicationException {
    public ApplicationResourceNotFoundException(ApplicationErrorCode errorCode) {
        super(errorCode);
    }
}
