package com.lilamaris.stockwolf.inventory.application.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum InventoryErrorCode {
    UNIDENTIFIED_ACTOR("C001", "사용자를 식별할 수 없습니다.");

    private final String code;
    private final String message;
}
