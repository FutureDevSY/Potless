package Potless.Backend.road.entity.road;

import Potless.Backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageEntity extends BaseEntity {

    @Id
    @Column(name = "image_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "damage_id")
    private DamageEntity damageEntity;

    @Column(name = "image_url", nullable = false)
    private String url;

    @Column(name = "image_order", nullable = false)
    private Integer order;

    @Builder
    public ImageEntity(Long id, DamageEntity damageEntity, String url, Integer order) {
        this.id = id;
        this.damageEntity = damageEntity;
        this.url = url;
        this.order = order;
    }
}
