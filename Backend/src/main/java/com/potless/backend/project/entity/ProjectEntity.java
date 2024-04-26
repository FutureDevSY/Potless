package com.potless.backend.project.entity;

import com.potless.backend.damage.entity.enums.Status;
import com.potless.backend.global.entity.BaseEntity;
import com.potless.backend.member.entity.ManagerEntity;
import com.potless.backend.member.entity.TeamEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@Table(name = "project")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectEntity extends BaseEntity {

    @Id
    @Column(name = "location_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_name", nullable = false)
    private String projectName;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "manager_id", nullable = false)
    private ManagerEntity managerEntity;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "team_id")
    private TeamEntity teamEntity;

    @Column(name = "project_date", nullable = false)
    private LocalDate projectDate;

    @Column(name = "project_size", nullable = false)
    private Integer projectSize;

    @Column(name = "project_status", nullable = false)
    private Status status = Status.작업전;

    @Builder
    public ProjectEntity(Long id, String projectName, ManagerEntity managerEntity, TeamEntity teamEntity, LocalDate projectDate, Integer projectSize, Status status) {
        this.id = id;
        this.projectName = projectName;
        this.managerEntity = managerEntity;
        this.teamEntity = teamEntity;
        this.projectDate = projectDate;
        this.projectSize = projectSize;
        this.status = status;
    }

    public void changeStatus(Status status) {
        this.status = status;
    }
}

