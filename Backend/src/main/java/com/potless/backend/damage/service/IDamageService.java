package com.potless.backend.damage.service;

import com.potless.backend.damage.dto.controller.request.DamageSearchRequestDTO;
import com.potless.backend.damage.dto.controller.request.DamageVerificationRequestDTO;
import com.potless.backend.damage.dto.controller.response.DamageResponseDTO;
import com.potless.backend.damage.dto.service.request.DamageSetRequestServiceDTO;
import com.potless.backend.damage.dto.service.response.StatisticCountResponseDTO;
import com.potless.backend.damage.dto.service.response.StatisticListResponseDTO;
import com.potless.backend.damage.dto.service.response.StatisticLocationCountResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IDamageService {
    Page<DamageResponseDTO> getDamages(DamageSearchRequestDTO damageSearchRequestDTO, Pageable pageable);

    DamageResponseDTO getDamage(Long damageId);

    void setDamage(DamageSetRequestServiceDTO data);

    List<DamageResponseDTO> getDamageVerification(DamageVerificationRequestDTO data);

    void deleteDamage(Long damageId);

    StatisticLocationCountResponseDTO getStatisticLocation(String locationName);

    List<StatisticLocationCountResponseDTO> getStatisticLocations();

    StatisticListResponseDTO getStatistic(Long areaId);

    List<StatisticCountResponseDTO> getStatistics();

    void setImageForStatus(Long damageId, List<String> fileUrls);

    void setWorkDone(Long damageId);

//    List<DamageResponseDTO> getWorkDamage(Long memberId);
}
