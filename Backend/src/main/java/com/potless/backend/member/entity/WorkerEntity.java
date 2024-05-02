package com.potless.backend.member.entity;

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

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE, CascadeType.PERSIST}, orphanRemoval = true)
    @JoinColumn(name = "member_id")
    private MemberEntity memberEntity;

    @Column(name = "worker_name", nullable = false)
    private String workerName;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "team_id")
    private TeamEntity teamEntity;

    @Builder
    public WorkerEntity(Long id, String workerName, TeamEntity teamEntity) {
        this.id = id;
        this.workerName = workerName;
        this.teamEntity = teamEntity;
    }

    public void changeTeam(TeamEntity teamEntity) {
        this.teamEntity = teamEntity;
    }
}
