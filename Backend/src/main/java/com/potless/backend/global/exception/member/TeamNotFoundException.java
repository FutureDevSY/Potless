package com.potless.backend.global.exception.member;

import com.potless.backend.global.format.response.ErrorCode;
import lombok.Getter;

@Getter
public class TeamNotFoundException extends RuntimeException {
    private final ErrorCode errorCode;

    public TeamNotFoundException() {
        this.errorCode = ErrorCode.TEAM_NOT_FOUND;
    }
}
