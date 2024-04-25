package Potless.Backend.global.exception.member;

import Potless.Backend.global.format.response.ErrorCode;
import lombok.Getter;

@Getter
public class MemberNotFoundException extends RuntimeException {
    private final ErrorCode errorCode;
    public MemberNotFoundException(){this.errorCode = ErrorCode.MEMBER_NOT_FOUND;}
}
