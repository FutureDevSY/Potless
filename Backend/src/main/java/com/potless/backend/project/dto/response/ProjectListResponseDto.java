package com.potless.backend.project.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@Builder
@ToString
public class ProjectListResponseDto {
    private Long projectId;
    private String projectName;
    private String managerName;
    private LocalDate projectDate;
    private Integer projectSize;

    public ProjectListResponseDto(Long projectId, String projectName, String managerName, LocalDate projectDate, Integer projectSize) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.managerName = managerName;
        this.projectDate = projectDate;
        this.projectSize = projectSize;
    }
}
