package HarryPort.Backend.road.entity.road;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@DiscriminatorValue("POTHOLE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PotholeEntity extends RoadDamageEntity {

    @Column(name = "pothole_length")
    private Double length;

    @Builder
    public PotholeEntity(Long id, Integer severity, Double dirX, Double dirY, String address, String roadName, Status status, Double width, Double height, Double length) {
        super(id, severity, dirX, dirY, address, roadName, status, width, height);
        this.length = length;
    }
}
