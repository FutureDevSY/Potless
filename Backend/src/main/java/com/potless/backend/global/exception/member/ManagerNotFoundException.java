package com.potless.backend.global.exception.member;

import com.potless.backend.global.format.response.ErrorCode;
import lombok.Getter;

@Getter
public class ManagerNotFoundException extends RuntimeException {
    private final ErrorCode errorCode;

    public ManagerNotFoundException() {
        this.errorCode = ErrorCode.MANAGER_NOT_FOUND;
    }
}
