package com.potless.backend.global.exception.task;

import com.potless.backend.global.format.response.ErrorCode;
import lombok.Getter;

@Getter
public class TaskNotFoundException extends RuntimeException {
    private final ErrorCode errorCode;

    public TaskNotFoundException() {
        this.errorCode = ErrorCode.TASK_NOT_FOUND;
    }
}
