package com.potless.backend.global.exception.pothole;

import com.potless.backend.global.format.response.ErrorCode;
import lombok.Getter;

@Getter
public class PotholeAreaNotFoundException extends RuntimeException {

    private final ErrorCode errorCode;

    public PotholeAreaNotFoundException() {
        this.errorCode = ErrorCode.POTHOLE_AREA_NOT_FOUND;
    }
}
