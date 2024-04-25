package com.potless.backend.member.repository.member;

import com.potless.backend.member.entity.MemberEntity;
import com.potless.backend.member.repository.member.custom.MemberRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberEntity, Long>, MemberRepositoryCustom {

}
