package com.lilamaris.stockwolf.inventory.domain.exception;

public class DomainIllegalArgumentException extends DefaultDomainException {
    public DomainIllegalArgumentException(ErrorCode errorCode) {
        super(errorCode);
    }
}
