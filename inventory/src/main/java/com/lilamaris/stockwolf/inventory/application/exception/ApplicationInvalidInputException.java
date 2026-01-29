package com.lilamaris.stockwolf.inventory.application.exception;

public class ApplicationInvalidInputException extends DefaultApplicationException {
    public ApplicationInvalidInputException(ApplicationErrorCode errorCode) {
        super(errorCode);
    }
}
