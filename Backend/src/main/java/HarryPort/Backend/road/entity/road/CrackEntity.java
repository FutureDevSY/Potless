package HarryPort.Backend.road.entity.road;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@DiscriminatorValue("CRACK")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CrackEntity extends RoadDamageEntity {
    
    @Builder
    public CrackEntity(Long id, Integer severity, Double dirX, Double dirY, String address, String roadName, Status status, Double width, Double height) {
        super(id, severity, dirX, dirY, address, roadName, status, width, height);
    }
}