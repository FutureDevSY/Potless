package com.potless.backend.project.entity;

import com.potless.backend.damage.entity.road.DamageEntity;
import com.potless.backend.global.entity.BaseEntity;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Table(name = "task")
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Tag(name = "Task 컨트롤러", description = "Task Controller API")
public class TaskEntity extends BaseEntity {

    @Id
    @Column(name = "task_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "project_id", nullable = false)
    private ProjectEntity projectEntity;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "damage_id", nullable = false)
    private DamageEntity damageEntity;

    @Column(name = "task_order", nullable = false)
    private int taskOrder;

    @Builder
    public TaskEntity(Long id, ProjectEntity projectEntity, DamageEntity damageEntity, Integer taskOrder) {
        this.id = id;
        this.projectEntity = projectEntity;
        this.damageEntity = damageEntity;
        this.taskOrder = taskOrder;
    }
}
