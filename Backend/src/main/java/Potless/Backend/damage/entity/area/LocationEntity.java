package Potless.Backend.damage.entity.area;

import Potless.Backend.damage.entity.road.DamageEntity;
import Potless.Backend.global.entity.BaseEntity;
import com.querydsl.core.annotations.QueryProjection;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "location")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LocationEntity extends BaseEntity {

    @Id
    @Column(name = "location_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "location_name", nullable = false)
    private String locationName;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "area_id")
    private AreaEntity areaEntity;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "locationEntity")
    private List<DamageEntity> damageEntities = new ArrayList<>();

    @Builder
    @QueryProjection
    public LocationEntity(Long id, String locationName, AreaEntity areaEntity, List<DamageEntity> damageEntities) {
        this.id = id;
        this.locationName = locationName;
        this.areaEntity = areaEntity;
        this.damageEntities = damageEntities;
    }

    public void changeArea(AreaEntity areaEntity) {
        this.areaEntity = areaEntity;
    }
}
