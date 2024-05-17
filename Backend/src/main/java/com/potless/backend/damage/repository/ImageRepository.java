package com.potless.backend.damage.repository;

import com.potless.backend.damage.entity.road.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<ImageEntity, Long> {
    List<ImageEntity> findByDamageEntityId(Long damageId);

    @Modifying
    @Query(value = "DELETE FROM image WHERE image_id IN :imageIds", nativeQuery = true)
    void deleteByIds(List<Long> imageIds);
}