package com.potless.backend.damage.service;

import com.potless.backend.damage.dto.controller.request.DamageSetRequestDTO;
import com.potless.backend.damage.entity.road.DamageEntity;
import com.potless.backend.damage.repository.DamageRepository;
import com.potless.backend.global.exception.pothole.DuplPotholeException;
import com.potless.backend.hexagon.service.HexagonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DuplicateAreaService {

    private final HexagonService hexagonService;
    private final DamageRepository damageRepository;

    // 1차
    @Transactional(readOnly = true)
    public String checkIsDuplicated(DamageSetRequestDTO damageSetRequestDTO) {
        String hexagonIndex = hexagonService.getH3Index(damageSetRequestDTO.getY(), damageSetRequestDTO.getX(), 13);
        Optional<DamageEntity> optionalDamageEntity =
                damageRepository.findDamageByHexagonIndexAndDtype(hexagonIndex, damageSetRequestDTO.getDtype());

        if (optionalDamageEntity.isPresent()) {
            DamageEntity damageEntity = optionalDamageEntity.get();

            // 프론트 요청 6번째 자리 까지 똑같다 그러면 동일 요청이라 판단 해서 count 안늘리게
            if (!Objects.equals(damageEntity.getDirX(), damageSetRequestDTO.getX())
                    && !Objects.equals(damageEntity.getDirY(), damageSetRequestDTO.getY())) {
                damageEntity.addCount();
                damageRepository.save(damageEntity);
            }
            throw new DuplPotholeException();
        }
        return hexagonIndex;
    }
}
