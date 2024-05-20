package com.potless.backend.member.entity;

import com.potless.backend.damage.entity.area.AreaEntity;
import com.potless.backend.global.entity.MemberBaseEntity;
import com.potless.backend.member.dto.SignupRequestDto;
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

    /* 0은 관리자, 1은 작업자, 3은 일반회원 */
    @Column(name = "member_role", nullable = false)
    private Integer role;

    @Column(name = "member_email", nullable = false)
    private String email;

    @Column(name = "member_phone", nullable = false)
    private String phone;

    @Column(name = "member_password", nullable = false)
    private String password;

    @OneToOne(mappedBy = "memberEntity", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE, CascadeType.PERSIST}, orphanRemoval = true)
    private ManagerEntity managerEntity;

    @Column(name = "member_profile_url")
    private String profileUrl;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "area_id")
    private AreaEntity area;

    @Builder
    public MemberEntity(Long id, String memberName, Integer role, String email, String phone, String password, AreaEntity area, ManagerEntity managerEntity, String profileUrl) {
        Id = id;
        this.memberName = memberName;
        this.role = role;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.area = area;
        this.managerEntity = managerEntity;
        this.profileUrl = profileUrl;
    }

    public static MemberEntity of(SignupRequestDto requestDto, String encodedPassword, AreaEntity area) {
        return MemberEntity.builder()
                .email(requestDto.getEmail())
                .password(encodedPassword)
                .memberName(requestDto.getMemberName())
                .role(3)    //회원가입이 일반 사용자들을 위함이므로 3
                .area(area)
                .phone(requestDto.getPhone())
                .build();
    }

}
