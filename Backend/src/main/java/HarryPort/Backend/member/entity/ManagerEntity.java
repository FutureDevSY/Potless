package HarryPort.Backend.member.entity;

import HarryPort.Backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "manager")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ManagerEntity extends BaseEntity {

    @Id
    @Column(name = "manager_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "manager_area_id")
    private Long areaId;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE, CascadeType.PERSIST}, orphanRemoval = true)
    @JoinColumn(name = "member_id")
    private MemberEntity memberEntity;

    @OneToOne(mappedBy = "managerEntity", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE, CascadeType.PERSIST}, orphanRemoval = true)
    private WorkerEntity workerEntity;
}