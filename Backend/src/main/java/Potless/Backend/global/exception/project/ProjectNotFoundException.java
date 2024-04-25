package Potless.Backend.global.exception.project;

import Potless.Backend.global.format.response.ErrorCode;
import lombok.Getter;

@Getter
public class ProjectNotFoundException extends RuntimeException {
    private final ErrorCode errorCode;
    public ProjectNotFoundException(){this.errorCode = ErrorCode.PROJECT_NOT_FOUND;}
}
