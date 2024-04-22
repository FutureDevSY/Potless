package Potless.Backend.global.exception.member;

import Potless.Backend.global.format.response.ErrorCode;
import lombok.Getter;

@Getter
public class InvalidPasswordException extends RuntimeException {

    private final ErrorCode errorCode;

    public InvalidPasswordException() {
        this.errorCode = ErrorCode.PASSWORD_MISMATCH;
    }
}
