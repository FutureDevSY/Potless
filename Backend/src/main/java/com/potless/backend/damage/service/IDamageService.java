package com.potless.backend.damage.service;

import com.potless.backend.damage.dto.controller.request.AreaDamageCountForDateRequestDTO;
import com.potless.backend.damage.dto.controller.request.DamageSearchRequestDTO;
import com.potless.backend.damage.dto.controller.response.DamageResponseDTO;
import com.potless.backend.damage.dto.service.request.AreaDamageCountForMonthServiceRequestDTO;
import com.potless.backend.damage.dto.service.request.DamageSetRequestServiceDTO;
import com.potless.backend.damage.dto.service.response.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IDamageService {
    Page<DamageResponseDTO> getDamages(DamageSearchRequestDTO damageSearchRequestDTO, Pageable pageable);

    DamageResponseDTO getDamage(Long damageId);

    void setDamage(DamageSetRequestServiceDTO data);

    StatisticLocationCountResponseDTO getStatisticLocation(String locationName);

    List<StatisticLocationCountResponseDTO> getStatisticLocations();

    StatisticListResponseDTO getStatistic(Long areaId);

    List<StatisticCountResponseDTO> getStatistics();

    void setImageForStatus(Long damageId, List<String> fileUrls);

    void setWorkDone(Long damageId);

    AreaForDateListResponseDTO getAreaDamageCountForDate(AreaDamageCountForDateRequestDTO areaDamageCountForDateRequestDTO);

    AreaForMonthListResponseDTO getAreaDamageCountForMonth(AreaDamageCountForMonthServiceRequestDTO areaDamageCountForMonthServiceRequestDTO);

    List<String> deleteDamage(List<Long> damageIds);

    List<String> setChangeImage(Long damageId, List<String> fileUrls);
    
    void setAsyncDamage(DamageSetRequestServiceDTO serviceDTO);
//    List<DamageResponseDTO> getWorkDamage(Long memberId);
}
