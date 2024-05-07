package com.potless.backend.project.dto.response;

import com.potless.backend.damage.dto.controller.response.DamageResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
    @Schema(description = "작업할 포트홀 개수")
    private Integer projectSize;
    @Schema(description = "작업할 포트홀 리스트")
    private List<DamageResponseDTO> damageResponseDTOS;
    @Schema(description = "Task ID정보")
    private List<TaskDetailDto> taskResponses;

    @Builder

    public ProjectDetailResponseDto(String projectName, String managerName, Integer projectSize, List<DamageResponseDTO> damageResponseDTOS, List<TaskDetailDto> taskResponses) {
        this.projectName = projectName;
        this.managerName = managerName;
        this.projectSize = projectSize;
        this.damageResponseDTOS = damageResponseDTOS;
        this.taskResponses = taskResponses;
    }
}
