package com.potless.backend.damage.service;

import com.potless.backend.damage.dto.controller.request.DamageSearchRequestDTO;
import com.potless.backend.damage.dto.controller.request.DamageVerificationRequestDTO;
import com.potless.backend.damage.dto.controller.response.DamageResponseDTO;
import com.potless.backend.damage.dto.service.request.DamageSetRequestServiceDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IDamageService {
    Page<DamageResponseDTO> getDamages(DamageSearchRequestDTO damageSearchRequestDTO, Pageable pageable);

    DamageResponseDTO getDamage(Long damageId);

    void setDamage(DamageSetRequestServiceDTO data);

    List<DamageResponseDTO> getDamageVerification(DamageVerificationRequestDTO data);

    void deleteDamage(Long damageId);
}
