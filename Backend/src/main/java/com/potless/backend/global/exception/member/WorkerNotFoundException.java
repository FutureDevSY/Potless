package com.potless.backend.global.exception.member;

import com.potless.backend.global.format.response.ErrorCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WorkerNotFoundException extends RuntimeException{
    private final ErrorCode errorCode;

    public WorkerNotFoundException() {
        this.errorCode = ErrorCode.MEMBER_NOT_FOUND;
    }
}
