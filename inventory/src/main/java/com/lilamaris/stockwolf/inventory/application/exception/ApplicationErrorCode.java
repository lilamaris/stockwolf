package com.lilamaris.stockwolf.inventory.application.exception;

import com.lilamaris.stockwolf.inventory.domain.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApplicationErrorCode implements ErrorCode {
    UNIDENTIFIED_ACTOR("AC001", "사용자를 식별할 수 없습니다."),

    INVENTORY_NOT_FOUND("AR001", "제품을 찾을 수 없습니다."),
    RESERVATION_NOT_FOUND("AR002", "예약을 찾을 수 없습니다."),
    ;
    private final String code;
    private final String message;
}
