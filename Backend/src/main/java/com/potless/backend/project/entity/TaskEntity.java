package com.potless.backend.project.entity;

import com.potless.backend.damage.entity.road.DamageEntity;
import com.potless.backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "task")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TaskEntity extends BaseEntity {

    @Id
    @Column(name = "task_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "project_id", nullable = false)
    private ProjectEntity projectEntity;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "damage_id", nullable = false)
    private DamageEntity damageEntity;

    @Builder
    public TaskEntity(Long id, ProjectEntity projectEntity, DamageEntity damageEntity) {
        this.id = id;
        this.projectEntity = projectEntity;
        this.damageEntity = damageEntity;
    }
}
