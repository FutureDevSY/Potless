package com.potless.backend.global.exception.pothole;

import com.potless.backend.global.format.response.ErrorCode;
import lombok.Getter;

@Getter
public class PotholeDetectionFailException extends RuntimeException {
    private final ErrorCode errorCode;

    public PotholeDetectionFailException() {
        this.errorCode = ErrorCode.POTHOLE_DETECTION_FAILED;
    }
}
