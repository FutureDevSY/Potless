package com.potless.backend.damage.repository;


import com.potless.backend.damage.dto.controller.request.AreaDamageCountForDateRequestDTO;
import com.potless.backend.damage.dto.controller.request.DamageSearchRequestDTO;
import com.potless.backend.damage.dto.controller.response.DamageResponseDTO;
import com.potless.backend.damage.dto.service.request.AreaDamageCountForMonthServiceRequestDTO;
import com.potless.backend.damage.dto.service.response.*;
import com.potless.backend.damage.entity.enums.Status;
import com.potless.backend.damage.entity.road.DamageEntity;
import com.potless.backend.flutter.dto.service.response.DamageAppResponseDTO;
import com.potless.backend.project.entity.TaskEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface DamageRepositoryCustom {
    Page<DamageResponseDTO> findDamagesWithLatestTransaction(DamageSearchRequestDTO damageSearchRequestDTO, Pageable pageable);

    StatisticLocationCountResponseDTO getStatisticLocation(String locationName);

    List<StatisticLocationCountResponseDTO> getStatisticLocations();

    StatisticListResponseDTO getStatistic(Long areaId);

    List<StatisticCountResponseDTO> getStatistics();

    AreaForDateListResponseDTO getAreaDamageCountForDate(AreaDamageCountForDateRequestDTO areaDamageCountForDateRequestDTO);

    AreaForMonthListResponseDTO getAreaDamageCountForMonth(AreaDamageCountForMonthServiceRequestDTO areaDamageCountForMonthServiceRequestDTO);

    Optional<DamageEntity> findDamageByHexagonIndexAndDtype(String hexagonIndex, String dtype);

    List<TaskEntity> findTasksByProjectIdAndDamageStatus(Long projectId, Status status);

    List<DamageAppResponseDTO> findByHexagonIndexIn(List<String> hexagonIndexes);

    List<DamageAppResponseDTO> findByHexagonIndex(String hexagonIndex);

    SeverityAreaResponseDTO countTodaysDamagesBySeverity(Long areaId);
}
