package com.potless.backend.project.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class TaskDetailDto {
    private Long taskId;
    private Long damageId;

    public TaskDetailDto(Long taskId, Long damageId) {
        this.taskId = taskId;
        this.damageId = damageId;
    }
}
