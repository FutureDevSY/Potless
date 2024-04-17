package HarryPort.Backend.road.entity.road;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "damage")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype", discriminatorType = DiscriminatorType.STRING)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class RoadDamageEntity {

    @Id
    @Column(name = "damage_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "damage_severity")
    private Integer severity;

    @Column(name = "damage_dir_x")
    private Double dirX;

    @Column(name = "damage_dir_y")
    private Double dirY;

    @Column(name = "damage_address")
    private String address;

    @Column(name = "damage_road_name")
    private String roadName;

    @Enumerated(EnumType.STRING)
    @Column(name = "damage_status")
    private Status status;

    @Column(name = "damage_width")
    private Double width;

    @Column(name = "damage_height")
    private Double height;

    // 이미지 내일
    
    public RoadDamageEntity(Long id, Integer severity, Double dirX, Double dirY, String address, String roadName, Status status, Double width, Double height) {
        this.id = id;
        this.severity = severity;
        this.dirX = dirX;
        this.dirY = dirY;
        this.address = address;
        this.roadName = roadName;
        this.status = status;
        this.width = width;
        this.height = height;
    }
}
