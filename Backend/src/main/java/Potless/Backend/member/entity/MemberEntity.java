package Potless.Backend.member.entity;

import Potless.Backend.global.entity.MemberBaseEntity;
import Potless.Backend.member.dto.SignupRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberEntity extends MemberBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", nullable = false)
    private Long Id;

    @Column(name = "member_name", nullable = false)
    private String memberName;

    /* 0은 관리자, 1은 일반 회원 */
    @Column(name = "member_role", nullable = false)
    private Integer role;

    @Column(name = "member_email", nullable = false)
    private String email;

    @Column(name = "member_phone", nullable = false)
    private String phone;

    @Column(name = "member_password", nullable = false)
    private String password;

    @Column(name = "member_region", nullable = false)
    private Integer region;

    @OneToOne(mappedBy = "memberEntity", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE, CascadeType.PERSIST}, orphanRemoval = true)
    private ManagerEntity managerEntity;

    @OneToOne(mappedBy = "memberEntity", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE, CascadeType.PERSIST}, orphanRemoval = true)
    private WorkerEntity workerEntity;

    @Builder
    public MemberEntity(Long id, String memberName, Integer role, String email, String phone, String password, Integer region, ManagerEntity managerEntity, WorkerEntity workerEntity) {
        Id = id;
        this.memberName = memberName;
        this.role = role;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.region = region;
        this.managerEntity = managerEntity;
        this.workerEntity = workerEntity;
    }

    public static MemberEntity of(SignupRequestDto requestDto, String encodedPassword) {
        return MemberEntity.builder()
                .email(requestDto.getEmail())
                .password(encodedPassword)
                .memberName(requestDto.getMemberName())
                .role(0)
                .phone(requestDto.getPhone())
                .region(requestDto.getRegion())
                .build();
    }

}
