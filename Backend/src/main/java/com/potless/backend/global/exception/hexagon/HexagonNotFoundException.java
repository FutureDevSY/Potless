package com.potless.backend.global.exception.hexagon;

import com.potless.backend.global.format.response.ErrorCode;
import lombok.Getter;

@Getter
public class HexagonNotFoundException extends RuntimeException {
    private final ErrorCode errorCode;
    public HexagonNotFoundException() {
        this.errorCode = ErrorCode.HEXAGON_NOT_FOUND;
    }
}
