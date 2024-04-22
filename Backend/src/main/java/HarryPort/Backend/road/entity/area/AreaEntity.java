package HarryPort.Backend.road.entity.area;

import HarryPort.Backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "area")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AreaEntity extends BaseEntity {

    @Id
    @Column(name = "area_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "area_gu")
    private String areaGu;

    @Column(name = "area_pothole_cnt")
    private Long potholeCnt;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "areaEntity")
    private List<LocationEntity> locationEntities = new ArrayList<>();

    @Builder
    public AreaEntity(Long id, String areaGu, Long potholeCnt, List<LocationEntity> locationEntities) {
        this.id = id;
        this.areaGu = areaGu;
        this.potholeCnt = potholeCnt;
        this.locationEntities = locationEntities;
    }
}
