package com.potless.backend.global.exception.member;

import com.potless.backend.global.format.response.ErrorCode;
import lombok.Getter;

@Getter
public class InvalidLoginAttemptException extends RuntimeException {

    private final ErrorCode errorCode;

    public InvalidLoginAttemptException() {
        this.errorCode = ErrorCode.LOGIN_FAILED;
    }
}
