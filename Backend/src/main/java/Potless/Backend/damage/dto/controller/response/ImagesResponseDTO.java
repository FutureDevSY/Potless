package Potless.Backend.damage.dto.controller.response;

import Potless.Backend.damage.entity.road.DamageEntity;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ImagesResponseDTO {

    private Long id;
    private DamageEntity damageEntity;
    private String url;
    private Integer order;

    @Builder
    @QueryProjection
    public ImagesResponseDTO(Long id, DamageEntity damageEntity, String url, Integer order) {
        this.id = id;
        this.damageEntity = damageEntity;
        this.url = url;
        this.order = order;
    }
}
