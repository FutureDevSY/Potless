package com.potless.backend.global.exception.jwt;

import com.potless.backend.global.format.response.ErrorCode;
import lombok.Getter;

@Getter
public class UnauthorizedAccessException extends RuntimeException {

    private final ErrorCode errorCode;

    public UnauthorizedAccessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
