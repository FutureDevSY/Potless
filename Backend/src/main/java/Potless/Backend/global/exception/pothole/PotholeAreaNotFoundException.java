package Potless.Backend.global.exception.pothole;

import Potless.Backend.global.format.response.ErrorCode;
import lombok.Getter;

@Getter
public class PotholeAreaNotFoundException extends RuntimeException {

    private final ErrorCode errorCode;

    public PotholeAreaNotFoundException() {
        this.errorCode = ErrorCode.POTHOLE_AREA_NOT_FOUND;
    }
}
