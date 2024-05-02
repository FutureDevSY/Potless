package com.potless.backend.member.entity;

import com.potless.backend.damage.entity.area.AreaEntity;
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
@Table(name = "manager")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ManagerEntity extends BaseEntity {

    @Id
    @Column(name = "manager_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "area_id")
    private AreaEntity areaEntity;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST}, orphanRemoval = true)
    @JoinColumn(name = "member_id")
    private MemberEntity memberEntity;

    @OneToMany(mappedBy = "managerEntity", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    private List<TeamEntity> teamEntities = new ArrayList<>();

    @OneToMany(mappedBy = "managerEntity", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    private List<ProjectEntity> projectEntities = new ArrayList<>();

    @Builder
    public ManagerEntity(Long id, AreaEntity areaEntity, MemberEntity memberEntity, List<TeamEntity> teamEntities, List<ProjectEntity> projectEntities) {
        this.id = id;
        this.areaEntity = areaEntity;
        this.memberEntity = memberEntity;
        this.teamEntities = teamEntities;
        this.projectEntities = projectEntities;
    }
}