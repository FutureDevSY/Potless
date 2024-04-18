package Potless.Backend.damage.entity.road;

import Potless.Backend.damage.entity.area.AreaEntity;
import Potless.Backend.damage.entity.area.LocationEntity;
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
    public CrackEntity(Long id, Integer severity, Double dirX, Double dirY, String address, String roadName, Double width, AreaEntity areaEntity, LocationEntity locationEntity, List<ImageEntity> imageEntities) {
        super(id, severity, dirX, dirY, address, roadName, width, areaEntity, locationEntity, imageEntities);
    }
}