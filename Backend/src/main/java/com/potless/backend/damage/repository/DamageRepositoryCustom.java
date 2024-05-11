package com.potless.backend.damage.repository;


import com.potless.backend.damage.dto.controller.request.AreaDamageCountForDateRequestDTO;
import com.potless.backend.damage.dto.controller.request.DamageSearchRequestDTO;
import com.potless.backend.damage.dto.controller.response.DamageResponseDTO;
import com.potless.backend.damage.dto.service.request.AreaDamageCountForMonthServiceRequestDTO;
import com.potless.backend.damage.dto.service.response.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DamageRepositoryCustom {
    Page<DamageResponseDTO> findDamagesWithLatestTransaction(DamageSearchRequestDTO damageSearchRequestDTO, Pageable pageable);

    StatisticLocationCountResponseDTO getStatisticLocation(String locationName);

    List<StatisticLocationCountResponseDTO> getStatisticLocations();

    StatisticListResponseDTO getStatistic(Long areaId);

    List<StatisticCountResponseDTO> getStatistics();

    AreaForDateListResponseDTO getAreaDamageCountForDate(AreaDamageCountForDateRequestDTO areaDamageCountForDateRequestDTO);

    AreaForMonthListResponseDTO getAreaDamageCountForMonth(AreaDamageCountForMonthServiceRequestDTO areaDamageCountForMonthServiceRequestDTO);

//    List<DamageResponseDTO> findDamagesByWorker(Long memberId);
}
