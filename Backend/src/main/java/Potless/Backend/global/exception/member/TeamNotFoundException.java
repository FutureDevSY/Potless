package Potless.Backend.global.exception.member;

import Potless.Backend.global.format.response.ErrorCode;
import lombok.Getter;

@Getter
public class TeamNotFoundException extends RuntimeException {
    private final ErrorCode errorCode;
    public TeamNotFoundException(){this.errorCode = ErrorCode.TEAM_NOT_FOUND;}
}
