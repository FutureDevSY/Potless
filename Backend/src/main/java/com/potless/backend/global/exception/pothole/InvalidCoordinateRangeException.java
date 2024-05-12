package com.potless.backend.global.exception.pothole;

import com.potless.backend.global.format.response.ErrorCode;

public class InvalidCoordinateRangeException extends RuntimeException{
    private final ErrorCode errorCode;

    public InvalidCoordinateRangeException() {
        this.errorCode = ErrorCode.INVALID_COORDINATE_RANGE;
    }
}
