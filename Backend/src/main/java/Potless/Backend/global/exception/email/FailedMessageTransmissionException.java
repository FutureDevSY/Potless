package Potless.Backend.global.exception.email;

import Potless.Backend.global.format.response.ErrorCode;
import lombok.Getter;

@Getter
public class FailedMessageTransmissionException extends RuntimeException {

    private final ErrorCode errorCode;

    public FailedMessageTransmissionException() {
        this.errorCode = ErrorCode.EMAIL_SEND_FAILED;
    }
}
