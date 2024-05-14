package com.potless.backend.hexagon.repository;

import com.potless.backend.hexagon.entity.HexagonEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HexagonRepository extends JpaRepository<HexagonEntity, Long> {

    HexagonEntity findByHexagonIndex(String hexagonIndex);
}
