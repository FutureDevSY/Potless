package com.potless.backend.global.exception.project;

import com.potless.backend.global.format.response.ErrorCode;
import lombok.Getter;

@Getter
public class AreaNotFoundException extends RuntimeException {
    private final ErrorCode errorCode;

    public AreaNotFoundException() {
        this.errorCode = ErrorCode.PROJECT_AREA_NOT_FOUND;
    }
}
