package com.potless.backend.project.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TaskDetailDto {
    private Long taskId;
    private Long damageId;

    public TaskDetailDto(Long taskId, Long damageId) {
        this.taskId = taskId;
        this.damageId = damageId;
    }
}
