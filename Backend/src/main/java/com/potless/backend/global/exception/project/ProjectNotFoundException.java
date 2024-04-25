package com.potless.backend.global.exception.project;

import com.potless.backend.global.format.response.ErrorCode;
import lombok.Getter;

@Getter
public class ProjectNotFoundException extends RuntimeException {
    private final ErrorCode errorCode;

    public ProjectNotFoundException() {
        this.errorCode = ErrorCode.PROJECT_NOT_FOUND;
    }
}
