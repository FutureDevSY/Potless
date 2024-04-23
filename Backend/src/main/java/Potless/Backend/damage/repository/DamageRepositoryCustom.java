package Potless.Backend.damage.repository;

import Potless.Backend.damage.dto.controller.request.DamageSearchRequestDTO;
import Potless.Backend.damage.dto.controller.response.DamageResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DamageRepositoryCustom {
    Page<DamageResponseDTO> findDamagesWithLatestTransaction(DamageSearchRequestDTO damageSearchRequestDTO, Pageable pageable);
}
