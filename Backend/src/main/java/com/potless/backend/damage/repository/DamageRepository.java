package com.potless.backend.damage.repository;


import com.potless.backend.damage.dto.controller.response.DamageResponseDTO;
import com.potless.backend.damage.entity.road.DamageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DamageRepository extends JpaRepository<DamageEntity, Long>, DamageRepositoryCustom, QuerydslPredicateExecutor<DamageEntity> {

    @Query("SELECT new com.potless.backend.damage.dto.controller.response.DamageResponseDTO(" +
            "d.id, d.severity, d.dirX, d.dirY, d.address, d.width, d.status, " +
            "area.areaGu, location.locationName, d.dtype) " +
            "FROM DamageEntity d " +
            "LEFT JOIN d.areaEntity area " +
            "LEFT JOIN d.locationEntity location " +
            "WHERE d.id = :damageId")
    DamageResponseDTO findDamageDetailsByIdSimple(@Param("damageId") Long damageId);

    @Query("SELECT new com.potless.backend.damage.dto.controller.response.DamageResponseDTO(" +
            "d.id, " +
            "d.severity, " +
            "d.dirX, " +
            "d.dirY, " +
            "d.address, " +
            "d.width, " +
            "d.status, " +
            "area.areaGu, " +
            "location.locationName, " +
            "(SELECT new com.potless.backend.damage.dto.controller.response.ImagesResponseDTO(img.id, img.url, img.order) FROM ImageEntity img WHERE img.damageEntity = d), " +
            "d.dtype) " +
            "FROM DamageEntity d " +
            "LEFT JOIN d.areaEntity area " +
            "LEFT JOIN d.locationEntity location " +
            "WHERE d.id IN :damageIds")
    List<DamageResponseDTO> findDamageDetailsByIds(@Param("damageIds") List<Long> damageIds);
}