package com.potless.backend.hexagon.repository;

import com.potless.backend.hexagon.entity.HexagonEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.transaction.annotation.Transactional;

public interface HexagonRepository extends JpaRepository<HexagonEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Transactional
    HexagonEntity findByHexagonIndex(String hexagonIndex);
}
