package com.potless.backend.project.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@NoArgsConstructor
@ToString
public class TaskAddRequestDto {
    private Long projectId;
    private List<Long> damageIds;
}
