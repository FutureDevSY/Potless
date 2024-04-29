package com.potless.backend.project.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class TaskAddRequestDto {
    private Long projectId;
    private Long damageId;
}
