package com.potless.backend.damage.service;

import com.potless.backend.damage.dto.controller.request.DamageSearchRequestDTO;
import com.potless.backend.damage.dto.controller.response.DamageResponseDTO;
import com.potless.backend.damage.dto.service.response.KakaoMapApiResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IDamageService {
    Page<DamageResponseDTO> getDamages(DamageSearchRequestDTO damageSearchRequestDTO, Pageable pageable);

    DamageResponseDTO getDamage(Long damageId);

    void setDamage(KakaoMapApiResponseDTO data);
}
