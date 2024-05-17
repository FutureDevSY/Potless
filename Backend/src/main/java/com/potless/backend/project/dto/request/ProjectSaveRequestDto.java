package com.potless.backend.project.dto.request;

import com.potless.backend.path.dto.Location;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Getter
@NoArgsConstructor
@Builder
public class ProjectSaveRequestDto {
    private Optional<Long> teamId;
    private String title;
    private LocalDate projectDate;
    private Long areaId;
    private List<Long> damageNums;
    private Location origin;

    public ProjectSaveRequestDto(Optional<Long> teamId, String title, LocalDate projectDate, Long areaId, List<Long> damageNums, Location origin) {
        if (title == null) {
            title = "도로 부속 작업 보고서";
        }

        this.teamId = teamId;
        this.title = title;
        this.projectDate = projectDate;
        this.areaId = areaId;
        this.damageNums = damageNums;
        this.origin = origin;
    }
}