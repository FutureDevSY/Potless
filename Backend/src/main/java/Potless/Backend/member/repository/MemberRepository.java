package Potless.Backend.member.repository;

import Potless.Backend.member.entity.MemberEntity;
import Potless.Backend.member.repository.custom.MemberRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberEntity, Long>, MemberRepositoryCustom {

}
