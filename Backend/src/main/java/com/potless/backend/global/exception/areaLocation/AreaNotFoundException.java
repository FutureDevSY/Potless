package com.potless.backend.global.exception.areaLocation;

import com.potless.backend.global.format.response.ErrorCode;
import lombok.Getter;

@Getter
public class AreaNotFoundException extends RuntimeException {

    private final ErrorCode errorCode;

    public AreaNotFoundException() {
        this.errorCode = ErrorCode.AREA_NOT_FOUND;
    }
}
