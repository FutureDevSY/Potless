package com.potless.backend.damage.entity.road;

import com.potless.backend.damage.entity.area.AreaEntity;
import com.potless.backend.damage.entity.area.LocationEntity;
import com.potless.backend.damage.entity.enums.Status;
import com.querydsl.core.annotations.QueryProjection;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@DiscriminatorValue("CRACK")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CrackEntity extends DamageEntity {

    @Builder
    @QueryProjection
    public CrackEntity(Long id, Integer severity, Double dirX, Double dirY, String address, Double width, Status status, AreaEntity areaEntity, LocationEntity locationEntity, List<ImageEntity> imageEntities, String dtype) {
        super(id, severity, dirX, dirY, address, width, status, areaEntity, locationEntity, imageEntities, dtype);
    }
}