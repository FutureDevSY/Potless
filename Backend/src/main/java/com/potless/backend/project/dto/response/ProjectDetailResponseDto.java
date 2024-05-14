package com.potless.backend.project.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
@ToString
@Schema(description = "단일 프로젝트 조회 DTO")
public class ProjectDetailResponseDto {
    @Schema(description = "프로젝트명")
    private String projectName;
    @Schema(description = "관리자명")
    private String managerName;
    @Schema(description = "팀이름")
    private String teamName;
    @Schema(description = "작업할 포트홀 개수")
    private Integer projectSize;
    private LocalDate projectDate;
    @Schema(description = "작업할 포트홀 리스트")
    private List<DamageDetailToProjectDto> DamageDetailToProjectDtos;

    @Builder

    public ProjectDetailResponseDto(String projectName, String managerName, String teamName, Integer projectSize, LocalDate projectDate, List<DamageDetailToProjectDto> damageDetailToProjectDtos) {
        this.projectName = projectName;
        this.managerName = managerName;
        this.teamName = teamName;
        this.projectSize = projectSize;
        this.projectDate = projectDate;
        DamageDetailToProjectDtos = damageDetailToProjectDtos;
    }
}

