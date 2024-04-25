package com.potless.backend.damage.repository;

import com.potless.backend.damage.entity.area.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<LocationEntity, Long> {

    Optional<LocationEntity> findByLocationName(String locationName);
}
