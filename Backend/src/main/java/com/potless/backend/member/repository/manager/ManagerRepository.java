package com.potless.backend.member.repository.manager;

import com.potless.backend.damage.entity.area.AreaEntity;
import com.potless.backend.member.entity.ManagerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ManagerRepository extends JpaRepository<ManagerEntity, Long> {
    @Query("SELECT m FROM ManagerEntity m WHERE m.memberEntity.id = :memberId")
    Optional<ManagerEntity> findByMemberId(@Param("memberId") Long memberId);

    Optional<ManagerEntity> findByAreaEntity(AreaEntity areaEntity);
}
