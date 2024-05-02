package com.potless.backend.damage.repository;


import com.potless.backend.damage.dto.controller.request.DamageSearchRequestDTO;
import com.potless.backend.damage.dto.controller.request.DamageVerificationRequestDTO;
import com.potless.backend.damage.dto.controller.response.DamageResponseDTO;
import com.potless.backend.damage.dto.service.response.StatisticCountResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DamageRepositoryCustom {
    Page<DamageResponseDTO> findDamagesWithLatestTransaction(DamageSearchRequestDTO damageSearchRequestDTO, Pageable pageable);

    List<DamageResponseDTO> findDamagesByVerificationRequest(DamageVerificationRequestDTO verificationRequest);

    StatisticCountResponseDTO getStatistic(String locationName);

    List<StatisticCountResponseDTO> getStatistics();

    StatisticCountResponseDTO getStatisticLocation(String locationName);

    List<StatisticCountResponseDTO> getStatisticLocations();
//    List<DamageResponseDTO> findDamagesByWorker(Long memberId);
}
