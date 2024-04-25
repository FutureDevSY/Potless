package com.potless.backend.global.exception.jwt;

import com.potless.backend.global.format.response.ErrorCode;
import lombok.Getter;

@Getter
public class AccessTokenNotFoundException extends RuntimeException {

    private final ErrorCode errorCode;

    public AccessTokenNotFoundException() {
        this.errorCode = ErrorCode.EXPIRED_TOKEN;
    }
}
