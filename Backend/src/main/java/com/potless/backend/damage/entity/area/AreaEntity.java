package com.potless.backend.damage.entity.area;

import com.potless.backend.damage.entity.road.DamageEntity;
import com.potless.backend.global.entity.BaseEntity;
import com.potless.backend.member.entity.ManagerEntity;
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
@Table(name = "area")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AreaEntity extends BaseEntity {

    @Column(name = "area_pothole_cnt", nullable = false)
    private final Long potholeCnt = 0L;
    @Id
    @Column(name = "area_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "area_gu", nullable = false)
    private String areaGu;
    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "areaEntity")
    private List<ManagerEntity> managerEntities = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "areaEntity")
    private List<LocationEntity> locationEntities = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "areaEntity")
    private List<DamageEntity> damageEntities = new ArrayList<>();

    @Builder
    @QueryProjection
    public AreaEntity(Long id, String areaGu, List<ManagerEntity> managerEntities, List<LocationEntity> locationEntities, List<DamageEntity> damageEntities) {
        this.id = id;
        this.areaGu = areaGu;
        this.managerEntities = managerEntities;
        this.locationEntities = locationEntities;
        this.damageEntities = damageEntities;
    }
}