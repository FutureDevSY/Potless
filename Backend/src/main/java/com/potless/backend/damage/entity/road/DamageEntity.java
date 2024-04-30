package com.potless.backend.damage.entity.road;

import com.potless.backend.damage.entity.area.AreaEntity;
import com.potless.backend.damage.entity.area.LocationEntity;
import com.potless.backend.damage.entity.enums.Status;
import com.potless.backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "damage", indexes = {
        @Index(name = "idx_damage_address", columnList = "damage_address"),
        @Index(name = "idx_damage_road_name", columnList = "damage_road_name"),
        @Index(name = "idx_damage_status", columnList = "damage_status"),
        @Index(name = "idx_damage_area_id", columnList = "area_id"),
        @Index(name = "idx_damage_location_id", columnList = "location_id")
})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype", discriminatorType = DiscriminatorType.STRING)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class DamageEntity extends BaseEntity {

    @Id
    @Column(name = "damage_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "damage_severity")
    private Integer severity;
    @Column(name = "damage_dir_x", nullable = false)
    private Double dirX;
    @Column(name = "damage_dir_y", nullable = false)
    private Double dirY;
    @Column(name = "damage_address", nullable = false)
    private String address;
    @Column(name = "damage_width", nullable = false)
    private Double width;
    @Enumerated(EnumType.STRING)
    @Column(name = "damage_status", nullable = false)
    private Status status = Status.작업전;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "area_id")
    private AreaEntity areaEntity;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "location_id")
    private LocationEntity locationEntity;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "damageEntity")
    private List<ImageEntity> imageEntities = new ArrayList<>();

    @Column(name = "dtype", insertable = false, updatable = false)
    private String dtype;

    public DamageEntity(Long id, Integer severity, Double dirX, Double dirY, String address, Double width, Status status, AreaEntity areaEntity, LocationEntity locationEntity, List<ImageEntity> imageEntities, String dtype) {
        this.id = id;
        this.severity = severity;
        this.dirX = dirX;
        this.dirY = dirY;
        this.address = address;
        this.width = width;
        this.status = status;
        this.areaEntity = areaEntity;
        this.locationEntity = locationEntity;
        this.imageEntities = imageEntities;
        this.dtype = dtype;
    }

    public void changeStatus(Status status) {
        this.status = status;
    }

}
