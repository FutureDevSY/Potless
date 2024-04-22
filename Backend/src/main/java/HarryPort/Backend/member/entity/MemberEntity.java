package HarryPort.Backend.member.entity;

import HarryPort.Backend.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long Id;

    @Column(name = "member_role")
    private Integer role;

    @Column(name = "member_email")
    private String email;

    @Column(name = "member_phone")
    private String phone;

    @Column(name = "member_password")
    private String password;

    @Column(name = "member_region")
    private Integer region;

    @OneToOne(mappedBy = "memberEntity", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE, CascadeType.PERSIST}, orphanRemoval = true)
    private ManagerEntity managerEntity;

    @OneToOne(mappedBy = "memberEntity", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE, CascadeType.PERSIST}, orphanRemoval = true)
    private WorkerEntity workerEntity;

    @Builder
    public MemberEntity(Long id, Integer role, String email, String phone, String password, Integer region, ManagerEntity managerEntity, WorkerEntity workerEntity) {
        Id = id;
        this.role = role;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.region = region;
        this.managerEntity = managerEntity;
        this.workerEntity = workerEntity;
    }
}
