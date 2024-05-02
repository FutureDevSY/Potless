package com.potless.backend.project.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Getter
@NoArgsConstructor
@Builder
@ToString
public class ProjectSaveRequestDto {
    private Long memberId;
    private Optional<Long> teamId;
    private String title;
    private LocalDate projectDate;
    private Long areaId;
    private List<Long> damageNums;

    public ProjectSaveRequestDto(Long memberId, Optional<Long> teamId, String title, LocalDate projectDate, Long areaId, List<Long> damageNums) {
        if (title == null) {
            title = "도로 부속 작업 보고서";
        }

        this.memberId = memberId;
        this.teamId = teamId;
        this.title = title;
        this.projectDate = projectDate;
        this.areaId = areaId;
        this.damageNums = damageNums;
    }
}