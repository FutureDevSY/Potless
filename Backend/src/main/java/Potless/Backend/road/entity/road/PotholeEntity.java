package Potless.Backend.road.entity.road;

import Potless.Backend.road.entity.area.AreaEntity;
import Potless.Backend.road.entity.area.LocationEntity;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@DiscriminatorValue("POTHOLE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PotholeEntity extends DamageEntity {

    @Builder
    public PotholeEntity(Long id, Integer severity, Double dirX, Double dirY, String address, String roadName, Double width, AreaEntity areaEntity, LocationEntity locationEntity, List<ImageEntity> imageEntities) {
        super(id, severity, dirX, dirY, address, roadName, width, areaEntity, locationEntity, imageEntities);
    }
}
