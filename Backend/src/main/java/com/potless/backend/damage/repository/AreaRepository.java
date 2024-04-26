package com.potless.backend.damage.repository;

import com.potless.backend.damage.entity.area.AreaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AreaRepository extends JpaRepository<AreaEntity, Long> {
    Optional<AreaEntity> findByAreaGu(String areaGu);

}