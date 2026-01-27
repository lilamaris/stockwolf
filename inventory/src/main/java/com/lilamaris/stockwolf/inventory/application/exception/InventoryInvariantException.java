package com.lilamaris.stockwolf.inventory.application.exception;

public class InventoryInvariantException extends InventoryApplicationException {
    public InventoryInvariantException(InventoryErrorCode errorCode) {
        super(errorCode);
    }
}
