package com.potless.backend.global.exception.pothole;

import com.potless.backend.global.format.response.ErrorCode;
import lombok.Getter;

@Getter
public class PotholeNotFoundException extends RuntimeException {

    private final ErrorCode errorCode;

    public PotholeNotFoundException() {
        this.errorCode = ErrorCode.POTHOLE_NOT_FOUND;
    }
}
