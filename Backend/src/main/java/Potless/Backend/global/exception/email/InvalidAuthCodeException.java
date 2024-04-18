package Potless.Backend.global.exception.email;

import Potless.Backend.global.format.response.ErrorCode;
import lombok.Getter;

@Getter
public class InvalidAuthCodeException extends RuntimeException {

    private final ErrorCode errorCode;

    public InvalidAuthCodeException() {
        this.errorCode = ErrorCode.AUTH_CODE_INVALID;
    }
}
