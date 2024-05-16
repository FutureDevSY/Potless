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
    @Column(name = "hexagon_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hexagon_index")
    private String hexagonIndex;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST}, mappedBy = "hexagonEntity")
    private List<DamageEntity> damageEntities = new ArrayList<>();

    @Builder
    public HexagonEntity(String hexagonIndex) {
        this.hexagonIndex = hexagonIndex;
    }
}