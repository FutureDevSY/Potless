package com.potless.backend.hexagon.repository;

import com.potless.backend.hexagon.entity.HexagonEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface HexagonRepository extends JpaRepository<HexagonEntity, Long> {

    HexagonEntity findByHexagonIndex(String hexagonIndex);
}
