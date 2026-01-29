package com.lilamaris.stockwolf.inventory.domain.exception;

public class DomainIllegalArgumentException extends DefaultDomainException {
    public DomainIllegalArgumentException(DomainErrorCode errorCode) {
        super(errorCode);
    }
}
