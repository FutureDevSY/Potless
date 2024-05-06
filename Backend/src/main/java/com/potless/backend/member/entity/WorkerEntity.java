package com.potless.backend.member.entity;

import com.potless.backend.damage.entity.area.AreaEntity;
import com.potless.backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "worker")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkerEntity extends BaseEntity {

    @Id
    @Column(name = "worker_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "member_id")
    private MemberEntity memberEntity;

    @Column(name = "worker_name", nullable = false)
    private String workerName;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "team_id")
    private TeamEntity teamEntity;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "area_id")
    private AreaEntity areaEntity;

    @Builder
    public WorkerEntity(Long id, MemberEntity memberEntity, String workerName, TeamEntity teamEntity, AreaEntity areaEntity) {
        this.id = id;
        this.memberEntity = memberEntity;
        this.workerName = workerName;
        this.teamEntity = teamEntity;
        this.areaEntity = areaEntity;
    }

    public void changeTeam(TeamEntity teamEntity) {
        this.teamEntity = teamEntity;
    }

    public void setTeamNull(){
        this.teamEntity = null;
    }
}
