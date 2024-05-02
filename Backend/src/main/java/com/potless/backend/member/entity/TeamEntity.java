package com.potless.backend.member.entity;

import com.potless.backend.global.entity.BaseEntity;
import com.potless.backend.project.entity.ProjectEntity;
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
    @Column(name = "team_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "manager_id")
    private ManagerEntity managerEntity;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST}, mappedBy = "teamEntity")
    private List<WorkerEntity> workerEntities = new ArrayList<>();

    @OneToMany(mappedBy = "teamEntity", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    private List<ProjectEntity> projectEntities = new ArrayList<>();

    @Column(name = "team_name", nullable = false)
    private String teamName;

    @Builder
    public TeamEntity(Long id, ManagerEntity managerEntity, String teamName, List<WorkerEntity> workerEntities, List<ProjectEntity> projectEntities) {
        this.id = id;
        this.teamName = teamName;
        this.managerEntity = managerEntity;
        this.workerEntities = workerEntities;
        this.projectEntities = projectEntities;
    }
}
