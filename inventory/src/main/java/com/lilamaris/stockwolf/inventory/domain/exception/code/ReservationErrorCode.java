package com.lilamaris.stockwolf.inventory.domain.exception.code;

import com.lilamaris.stockwolf.inventory.domain.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReservationErrorCode implements ErrorCode {
    INVALID_RESERVATION_STATUS("RS001", "변경 가능한 상태가 아닙니다."),
    INVALID_QUANTITY("RS002", "수량은 0보다 커야합니다.");
    private final String code;
    private final String message;
}
