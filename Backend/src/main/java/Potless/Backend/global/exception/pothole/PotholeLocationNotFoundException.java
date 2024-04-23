package Potless.Backend.global.exception.pothole;

import Potless.Backend.global.format.response.ErrorCode;
import lombok.Getter;

@Getter
public class PotholeLocationNotFoundException extends RuntimeException {

    private final ErrorCode errorCode;

    public PotholeLocationNotFoundException() {
        this.errorCode = ErrorCode.POTHOLE_LOCATION_NOT_FOUND;
    }
}
