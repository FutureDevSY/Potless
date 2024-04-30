package com.potless.backend.global.exception.pothole;

import com.potless.backend.global.format.response.ErrorCode;
import lombok.Getter;

@Getter
public class PotholeMinusException extends RuntimeException {

    private final ErrorCode errorCode;

    public PotholeMinusException() {
        this.errorCode = ErrorCode.POTHOLE_MINUS_NOT_FOUND;
    }
}
