package Potless.Backend.global.exception.jwt;

import Potless.Backend.global.format.response.ErrorCode;
import lombok.Getter;

@Getter
public class UnauthorizedAccessException extends RuntimeException {

    private final ErrorCode errorCode;

    public UnauthorizedAccessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
