package com.potless.backend.global.exception.member;

import com.potless.backend.global.format.response.ErrorCode;
import lombok.Getter;

@Getter
public class InvalidLoginAuthException extends RuntimeException{
    private final ErrorCode errorCode;

    public InvalidLoginAuthException() {
        this.errorCode = ErrorCode.LOGIN_AUTH_UNAUTHORIZED;
    }
}
