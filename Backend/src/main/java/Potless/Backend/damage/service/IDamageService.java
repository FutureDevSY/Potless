package Potless.Backend.damage.service;

import Potless.Backend.damage.dto.controller.request.DamageSearchRequestDTO;
import Potless.Backend.damage.dto.controller.response.DamageResponseDTO;
import Potless.Backend.damage.dto.service.response.KakaoMapApiResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IDamageService {
    Page<DamageResponseDTO> getDamages(DamageSearchRequestDTO damageSearchRequestDTO, Pageable pageable);

    DamageResponseDTO getDamage(Long damageId);

    void setDamage(KakaoMapApiResponseDTO data);
}
