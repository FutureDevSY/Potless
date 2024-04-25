package com.potless.backend.global.exception.member;

import com.potless.backend.global.format.response.ErrorCode;
import lombok.Getter;

@Getter
public class MemberNotFoundException extends RuntimeException {
    private final ErrorCode errorCode;

    public MemberNotFoundException() {
        this.errorCode = ErrorCode.MEMBER_NOT_FOUND;
    }
}
