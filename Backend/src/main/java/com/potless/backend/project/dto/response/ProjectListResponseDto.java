package com.potless.backend.project.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor
@Schema(description = "프로젝트 리스트 조회 DTO")
public class ProjectListResponseDto {
    @Schema(description = "프로젝트 ID")
    private Long projectId;
    @Schema(description = "프로젝트명")
    private String projectName;
    @Schema(description = "관리자명")
    private String managerName;
    @Schema(description = "프로젝트 날짜")
    private LocalDate projectDate;
    @Schema(description = "작업할 포트홀 개수")
    private Integer projectSize;
    @Schema(description = "생성 일자")
    private LocalDateTime createdDate;
    @Schema(description = "팀 이름")
    private String teamName;

    @QueryProjection
    public ProjectListResponseDto(Long projectId, String projectName, String managerName, LocalDate projectDate, Integer projectSize, LocalDateTime createdDate, String teamName) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.managerName = managerName;
        this.projectDate = projectDate;
        this.projectSize = projectSize;
        this.createdDate = createdDate;
        this.teamName = teamName;
    }

    public String formatDate(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date.format(formatter);
    }
}
