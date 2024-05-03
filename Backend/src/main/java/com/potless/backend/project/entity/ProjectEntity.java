package com.potless.backend.project.entity;

import com.potless.backend.damage.entity.area.AreaEntity;
import com.potless.backend.damage.entity.enums.Status;
import com.potless.backend.global.entity.BaseEntity;
import com.potless.backend.member.entity.ManagerEntity;
import com.potless.backend.member.entity.TeamEntity;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@ToString
@Table(name = "project")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Tag(name = "Project 컨트롤러", description = "Project Controller API")
public class ProjectEntity extends BaseEntity {

    @Id
    @Column(name = "project_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_name", nullable = false)
    private String projectName;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "manager_id", nullable = false)
    private ManagerEntity managerEntity;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "team_id")
    private TeamEntity teamEntity;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "area_id")
    private AreaEntity areaEntity;

    @Column(name = "project_date", nullable = false)
    private LocalDate projectDate;

    @Column(name = "project_size", nullable = false)
    private Integer projectSize;

    @Column(name = "project_status", nullable = false)
    private Status status = Status.작업전;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "projectEntity")
    private List<TaskEntity> taskEntities = new ArrayList<>();

    @Builder
    public ProjectEntity(Long id, String projectName, ManagerEntity managerEntity, TeamEntity teamEntity, LocalDate projectDate, Integer projectSize, Status status, AreaEntity areaEntity, List<TaskEntity> taskEntities) {
        this.id = id;
        this.projectName = projectName;
        this.managerEntity = managerEntity;
        this.teamEntity = teamEntity;
        this.projectDate = projectDate;
        this.projectSize = projectSize;
        this.status = status;
        this.areaEntity = areaEntity;
        this.taskEntities = taskEntities;
    }

    public void changeStatus(Status status) {
        this.status = status;
    }
}

