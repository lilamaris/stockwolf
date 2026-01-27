package com.lilamaris.stockwolf.inventory.application.exception;

import lombok.Getter;

@Getter
public class InventoryApplicationException extends RuntimeException {
    private final InventoryErrorCode errorCode;

    public InventoryApplicationException(InventoryErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
