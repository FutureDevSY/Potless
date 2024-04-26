package com.potless.backend.damage.repository;

import com.potless.backend.damage.entity.road.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<ImageEntity, Long> {
    List<ImageEntity> findByDamageEntityId(Long damageId);
}