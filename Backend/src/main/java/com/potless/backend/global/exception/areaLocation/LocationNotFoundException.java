package com.potless.backend.global.exception.areaLocation;

import com.potless.backend.global.format.response.ErrorCode;
import lombok.Getter;

@Getter
public class LocationNotFoundException extends RuntimeException {

    private final ErrorCode errorCode;

    public LocationNotFoundException() {
        this.errorCode = ErrorCode.LOCATION_NOT_FOUND;
    }
}
