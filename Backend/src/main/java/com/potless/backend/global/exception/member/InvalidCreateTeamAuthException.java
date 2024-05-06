package com.potless.backend.global.exception.member;

import com.potless.backend.global.format.response.ErrorCode;
import lombok.Getter;

@Getter
public class InvalidCreateTeamAuthException extends RuntimeException{
    private final ErrorCode errorCode;

    public InvalidCreateTeamAuthException() {
        this.errorCode = ErrorCode.CREATE_TEAM_FAILED;
    }
}
