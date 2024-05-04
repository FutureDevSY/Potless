package com.potless.backend.global.exception.member;

import com.potless.backend.global.format.response.ErrorCode;
import lombok.Getter;

@Getter
public class UnauthorizedRequestException extends RuntimeException{
    private final ErrorCode errorCode;
    public UnauthorizedRequestException() {
        this.errorCode = ErrorCode.UNAUTHORIZED_ACCESS_TO_SERVICE;
    }
}
