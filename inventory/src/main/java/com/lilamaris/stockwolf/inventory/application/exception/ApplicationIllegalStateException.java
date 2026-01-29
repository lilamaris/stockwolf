package com.lilamaris.stockwolf.inventory.application.exception;

public class ApplicationIllegalStateException extends DefaultApplicationException {
    public ApplicationIllegalStateException(ApplicationErrorCode errorCode) {
        super(errorCode);
    }
}
