package Potless.Backend.project.dto.request;

import Potless.Backend.project.entity.TaskEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
@Builder
public class ProjectSaveRequestDto {
    private Long managerId;
    private Long teamId;
    private String title;
    private LocalDate projectDate;
    private List<Long> damageNums;

    public ProjectSaveRequestDto(Long managerId, Long teamId, String title, LocalDate projectDate, List<Long> damageNums) {
        if (title == null) {
            title = "도로 부속 작업 보고서";
        }

        this.managerId = managerId;
        this.teamId = teamId;
        this.title = title;
        this.projectDate = projectDate;
        this.damageNums = damageNums;
    }
}