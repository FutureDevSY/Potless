package com.potless.backend.global.exception.pothole;

import com.potless.backend.global.format.response.ErrorCode;
import lombok.Getter;

@Getter
public class DuplPotholeException extends RuntimeException {
    private final ErrorCode errorCode;

    public DuplPotholeException() {
        this.errorCode = ErrorCode.DUPL_POTHOLE;
    }
}
