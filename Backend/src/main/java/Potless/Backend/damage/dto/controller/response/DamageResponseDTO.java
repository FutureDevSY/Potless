package Potless.Backend.damage.dto.controller.response;

import Potless.Backend.damage.entity.area.AreaEntity;
import Potless.Backend.damage.entity.area.LocationEntity;
import Potless.Backend.damage.entity.enums.Status;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.util.List;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DamageResponseDTO {

    private Long id;
    private Integer severity;
    private Double dirX;
    private Double dirY;
    private String address;
    private String roadName;
    private Double width;
    private Status status;
    private AreaEntity areaEntity;
    private LocationEntity locationEntity;
    private List<ImagesResponseDTO> imagesResponseDTOS;
    private String dtype;

    @Builder
    @QueryProjection
    public DamageResponseDTO(Long id, Integer severity, Double dirX, Double dirY, String address, String roadName, Double width, Status status, AreaEntity areaEntity, LocationEntity locationEntity, List<ImagesResponseDTO> imagesResponseDTOS, String dtype) {
        this.id = id;
        this.severity = severity;
        this.dirX = dirX;
        this.dirY = dirY;
        this.address = address;
        this.roadName = roadName;
        this.width = width;
        this.status = status;
        this.areaEntity = areaEntity;
        this.locationEntity = locationEntity;
        this.imagesResponseDTOS = imagesResponseDTOS;
        this.dtype = dtype;
    }
}
