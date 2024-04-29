package com.potless.backend.project.dto.response;

import com.potless.backend.damage.dto.controller.response.DamageResponseDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@NoArgsConstructor
@ToString
public class ProjectDetailResponseDto {
    private String projectName;
    private String managerName;
    private Integer projectSize;
    private List<DamageResponseDTO> damageResponseDTOS;

    @Builder
    public ProjectDetailResponseDto(String projectName, String managerName, Integer projectSize, List<DamageResponseDTO> damageResponseDTOS) {
        this.projectName = projectName;
        this.managerName = managerName;
        this.projectSize = projectSize;
        this.damageResponseDTOS = damageResponseDTOS;
    }
}
