package Potless.Backend.member.entity;

import Potless.Backend.global.entity.BaseEntity;
import Potless.Backend.road.entity.project.ProjectEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "team")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamEntity extends BaseEntity {

    @Id
    @Column(name = "team_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "manager_id")
    private ManagerEntity managerEntity;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "teamEntity")
    private List<WorkerEntity> workerEntities = new ArrayList<>();

    @OneToMany(mappedBy = "teamEntity", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private List<ProjectEntity> projectEntities = new ArrayList<>();

    @Builder
    public TeamEntity(Long id, ManagerEntity managerEntity, List<WorkerEntity> workerEntities, List<ProjectEntity> projectEntities) {
        this.id = id;
        this.managerEntity = managerEntity;
        this.workerEntities = workerEntities;
        this.projectEntities = projectEntities;
    }
}
