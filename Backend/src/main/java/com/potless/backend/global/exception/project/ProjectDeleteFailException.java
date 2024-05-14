package com.potless.backend.global.exception.project;

import com.potless.backend.global.format.response.ErrorCode;
import lombok.Getter;

@Getter
public class ProjectDeleteFailException extends RuntimeException{
    private final ErrorCode errorCode;

    public ProjectDeleteFailException() {
        this.errorCode = ErrorCode.PROJECT_DELETE_FAIL;
    }
}
