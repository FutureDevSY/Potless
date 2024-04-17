package HarryPort.Backend.road.entity.area;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "location")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LocationEntity {

    @Id
    @Column(name = "location_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "location_name")
    private String locationName;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "area_id")
    private AreaEntity areaEntity;

    @Builder
    public LocationEntity(Long id, String locationName, AreaEntity areaEntity) {
        this.id = id;
        this.locationName = locationName;
        this.areaEntity = areaEntity;
    }
}
