package Potless.Backend.damage.repository;

import Potless.Backend.damage.entity.area.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<LocationEntity, Long> {

    Optional<LocationEntity> findByLocationName(String locationName);
}
