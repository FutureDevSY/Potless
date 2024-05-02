package com.potless.backend.project.dto.response;

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
public class ProjectListResponseDto {
    private Long projectId;
    private String projectName;
    private String managerName;
    private LocalDate projectDate;
    private Integer projectSize;
    private String createdDate;

    public ProjectListResponseDto(Long projectId, String projectName, String managerName, LocalDate projectDate, Integer projectSize, LocalDateTime createdDate) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.managerName = managerName;
        this.projectDate = projectDate;
        this.projectSize = projectSize;
        this.createdDate = formatDate(createdDate);
    }

    public String formatDate(LocalDateTime date){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return date.format(formatter);
    }
}
