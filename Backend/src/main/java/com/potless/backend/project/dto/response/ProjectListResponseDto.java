package com.potless.backend.project.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor
@ToString
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
    private String createdDate;
    @Schema(description = "팀 이름")
    private String teamName;

    public ProjectListResponseDto(Long projectId, String projectName, String managerName, LocalDate projectDate, Integer projectSize, String createdDate, String teamName) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.managerName = managerName;
        this.projectDate = projectDate;
        this.projectSize = projectSize;
        this.createdDate = createdDate;
        this.teamName = teamName;
    }

    public String formatDate(LocalDateTime date){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return date.format(formatter);
    }
}
