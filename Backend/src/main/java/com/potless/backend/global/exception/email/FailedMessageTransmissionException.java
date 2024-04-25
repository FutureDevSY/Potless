package com.potless.backend.global.exception.email;

import com.potless.backend.global.format.response.ErrorCode;
import lombok.Getter;

@Getter
public class FailedMessageTransmissionException extends RuntimeException {

    private final ErrorCode errorCode;

    public FailedMessageTransmissionException() {
        this.errorCode = ErrorCode.EMAIL_SEND_FAILED;
    }
}
