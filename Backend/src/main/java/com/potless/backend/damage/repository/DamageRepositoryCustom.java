package com.potless.backend.damage.repository;


import com.potless.backend.damage.dto.controller.request.DamageSearchRequestDTO;
import com.potless.backend.damage.dto.controller.response.DamageResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DamageRepositoryCustom {
    Page<DamageResponseDTO> findDamagesWithLatestTransaction(DamageSearchRequestDTO damageSearchRequestDTO, Pageable pageable);
}
