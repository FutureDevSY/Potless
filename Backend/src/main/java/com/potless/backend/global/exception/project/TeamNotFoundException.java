package com.potless.backend.global.exception.project;

import com.potless.backend.global.format.response.ErrorCode;

public class TeamNotFoundException extends RuntimeException {
    private final ErrorCode errorCode;

    public TeamNotFoundException() {
        this.errorCode = ErrorCode.TEAM_NOT_FOUND;
    }
}
