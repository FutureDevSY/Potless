package Potless.Backend.road.entity.project;

import Potless.Backend.global.entity.BaseEntity;
import Potless.Backend.member.entity.ManagerEntity;
import Potless.Backend.member.entity.TeamEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Builder
    public ProjectEntity(Long id, String projectName, ManagerEntity managerEntity, TeamEntity teamEntity) {
        this.id = id;
        this.projectName = projectName;
        this.managerEntity = managerEntity;
        this.teamEntity = teamEntity;
    }
}

