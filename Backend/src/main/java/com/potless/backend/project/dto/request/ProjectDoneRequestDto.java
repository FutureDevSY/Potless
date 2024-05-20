package com.potless.backend.project.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectDoneRequestDto {
    @NotNull(message = "프로젝트 ID는 비어있을 수 없습니다.")
    private Long projectId;

    @Builder
    public ProjectDoneRequestDto(Long projectId) {
        this.projectId = projectId;
    }
}
