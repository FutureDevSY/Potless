package Potless.Backend.global.exception.member;

import Potless.Backend.global.format.response.ErrorCode;
import lombok.Getter;

@Getter
public class EmailNotFoundException extends RuntimeException {

    private final ErrorCode errorCode;

    public EmailNotFoundException() {
        this.errorCode = ErrorCode.EMAIL_NOT_FOUND;
    }
}
