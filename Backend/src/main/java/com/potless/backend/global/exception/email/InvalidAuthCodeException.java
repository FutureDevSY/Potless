package com.potless.backend.global.exception.email;

import com.potless.backend.global.format.response.ErrorCode;
import lombok.Getter;

@Getter
public class InvalidAuthCodeException extends RuntimeException {

    private final ErrorCode errorCode;

    public InvalidAuthCodeException() {
        this.errorCode = ErrorCode.AUTH_CODE_INVALID;
    }
}
