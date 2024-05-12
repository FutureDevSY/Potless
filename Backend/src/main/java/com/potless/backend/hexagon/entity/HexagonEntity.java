package com.potless.backend.hexagon.entity;

import com.potless.backend.damage.entity.road.DamageEntity;
import jakarta.persistence.*;
        import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "hexagon")
public class HexagonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 50, nullable = false)
    private Double latitude;
    @Column(length = 50, nullable = false)
    private Double longitude;
    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST}, mappedBy = "hexagonEntity")
    private List<DamageEntity> damageEntities = new ArrayList<>();

    @Builder
    public HexagonEntity(Long id, Double latitude, Double longitude, List<DamageEntity> damageEntities) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.damageEntities = damageEntities;
    }
}