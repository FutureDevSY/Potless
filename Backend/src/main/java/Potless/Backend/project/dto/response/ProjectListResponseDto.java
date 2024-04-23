package Potless.Backend.project.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@Builder
public class ProjectListResponseDto {
    private Long id;
    private String projectName;
    private String managerName;
    private LocalDate projectDate;
    private Integer projectSize;

    public ProjectListResponseDto(Long id, String projectName, String managerName, LocalDate projectDate, Integer projectSize) {
        this.id = id;
        this.projectName = projectName;
        this.managerName = managerName;
        this.projectDate = projectDate;
        this.projectSize = projectSize;
    }
}
