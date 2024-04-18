package Potless.Backend.global.exception.pothole;

import Potless.Backend.global.format.response.ErrorCode;
import lombok.Getter;

@Getter
public class PotholeNotFoundException extends RuntimeException {

    private final ErrorCode errorCode;
    public PotholeNotFoundException(){
        this.errorCode = ErrorCode.POTHOLE_NOT_FOUND;
    }
}
