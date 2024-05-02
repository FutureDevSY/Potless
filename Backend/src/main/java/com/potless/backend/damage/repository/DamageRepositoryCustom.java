package com.potless.backend.damage.repository;


import com.potless.backend.damage.dto.controller.request.DamageSearchRequestDTO;
import com.potless.backend.damage.dto.controller.request.DamageVerificationRequestDTO;
import com.potless.backend.damage.dto.controller.response.DamageResponseDTO;
import com.potless.backend.damage.dto.service.response.StatisticCountResponseDTO;
import com.potless.backend.damage.dto.service.response.StatisticListResponseDTO;
import com.potless.backend.damage.dto.service.response.StatisticLocationCountResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DamageRepositoryCustom {
    Page<DamageResponseDTO> findDamagesWithLatestTransaction(DamageSearchRequestDTO damageSearchRequestDTO, Pageable pageable);

    List<DamageResponseDTO> findDamagesByVerificationRequest(DamageVerificationRequestDTO verificationRequest);

    StatisticLocationCountResponseDTO getStatisticLocation(String locationName);

    List<StatisticLocationCountResponseDTO> getStatisticLocations();

    StatisticListResponseDTO getStatistic(Long areaId);

    List<StatisticCountResponseDTO> getStatistics();

//    List<DamageResponseDTO> findDamagesByWorker(Long memberId);
}
