package com.potless.backend.damage.repository;

import com.potless.backend.damage.dto.controller.response.DamageResponseDTO;
import com.potless.backend.damage.entity.road.DamageEntity;
import com.potless.backend.hexagon.entity.HexagonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface DamageRepository extends JpaRepository<DamageEntity, Long>, DamageRepositoryCustom, QuerydslPredicateExecutor<DamageEntity> {

    @Query("SELECT new com.potless.backend.damage.dto.controller.response.DamageResponseDTO(" +
            "d.id, d.severity, d.dirX, d.dirY, d.address, d.width, d.status, " +
            "area.areaGu, location.locationName, d.dtype, d.createdDateTime, d.memberEntity.Id, d.count) " +
            "FROM DamageEntity d " +
            "LEFT JOIN d.areaEntity area " +
            "LEFT JOIN d.locationEntity location " +
            "LEFT JOIN d.memberEntity member " +
            "WHERE d.id = :damageId")
    DamageResponseDTO findDamageDetailsByIdSimple(@Param("damageId") Long damageId);


    @Modifying
    @Query(value = "INSERT INTO damage (damage_severity, damage_dir_x, damage_dir_y, damage_address, damage_width, damage_status, area_id, location_id, hexagon_id, dtype, member_id, damage_count, created_date_time, modified_date_time) " +
            "SELECT :severity, :dirX, :dirY, :address, :width, :status, :areaId, :locationId, :hexagonId, :dtype, :memberId, 0, :createdDateTime, :modifiedDateTime " +
            "WHERE NOT EXISTS (SELECT 1 FROM damage WHERE hexagon_id = :hexagonId and dtype = :dtype LOCK IN SHARE MODE)", nativeQuery = true)
    void insertIfNotExistsWithLock(@Param("severity") Integer severity,
                                   @Param("dirX") Double dirX,
                                   @Param("dirY") Double dirY,
                                   @Param("address") String address,
                                   @Param("width") Double width,
                                   @Param("status") String status,
                                   @Param("areaId") Long areaId,
                                   @Param("locationId") Long locationId,
                                   @Param("hexagonId") Long hexagonId,
                                   @Param("dtype") String dtype,
                                   @Param("memberId") Long memberId,
                                   @Param("createdDateTime") LocalDateTime createdDateTime,
                                   @Param("modifiedDateTime") LocalDateTime modifiedDateTime);

    DamageEntity findTopByHexagonEntityOrderByCreatedDateTimeDesc(HexagonEntity hexagonEntity);

}