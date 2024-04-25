package Potless.Backend.project.dto.response;

import Potless.Backend.damage.dto.controller.response.DamageResponseDTO;
import Potless.Backend.damage.dto.controller.response.ImagesResponseDTO;
import Potless.Backend.damage.entity.area.AreaEntity;
import Potless.Backend.damage.entity.area.LocationEntity;
import Potless.Backend.damage.entity.enums.Status;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ProjectDetailResponseDto {
    private String projectName;
    private String managerName;
    private Integer projectSize;
    private  List<DamageResponseDTO> damageResponseDTOS;

    @Builder

    public ProjectDetailResponseDto(String projectName, String managerName, Integer projectSize, List<DamageResponseDTO> damageResponseDTOS) {
        this.projectName = projectName;
        this.managerName = managerName;
        this.projectSize = projectSize;
        this.damageResponseDTOS = damageResponseDTOS;
    }
}
