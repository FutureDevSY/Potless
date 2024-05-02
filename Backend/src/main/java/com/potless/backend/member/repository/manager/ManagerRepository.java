package com.potless.backend.member.repository.manager;

import com.potless.backend.member.entity.ManagerEntity;
import com.potless.backend.member.repository.manager.custom.ManagerRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ManagerRepository extends JpaRepository<ManagerEntity, Long>, ManagerRepositoryCustom {
    @Query("SELECT m FROM ManagerEntity m WHERE m.memberEntity.id = :memberId")
    Optional<ManagerEntity> findByMemberId(@Param("memberId") Long memberId);
}
