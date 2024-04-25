package Potless.Backend.damage.repository;

import Potless.Backend.damage.dto.controller.response.DamageResponseDTO;
import Potless.Backend.damage.entity.road.DamageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DamageRepository extends JpaRepository<DamageEntity, Long>, DamageRepositoryCustom, QuerydslPredicateExecutor<DamageEntity> {

    @Query("SELECT new Potless.Backend.damage.dto.controller.response.DamageResponseDTO(d.id, d.severity, d.dirX, d.dirY, d.address, d.roadName, d.width, d.status, area, location, images, d.dtype) " +
            "FROM DamageEntity d " +
            "LEFT JOIN d.areaEntity area " +
            "LEFT JOIN d.locationEntity location " +
            "LEFT JOIN d.imageEntities images " +
            "WHERE d.id = :damageId")
    DamageResponseDTO findDamageDetailsById(@Param("damageId") Long damageId);

    @Query("SELECT new Potless.Backend.damage.dto.controller.response.DamageResponseDTO(d.id, d.severity, d.dirX, d.dirY, d.address, d.roadName, d.width, d.status, area, location, images, d.dtype) " +
            "FROM DamageEntity d " +
            "LEFT JOIN d.areaEntity area " +
            "LEFT JOIN d.locationEntity location " +
            "LEFT JOIN d.imageEntities images " +
            "WHERE d.id IN :damageIds")
    List<DamageResponseDTO> findDamageDetailsByIds(@Param("damageIds") List<Long> damageIds);

}
