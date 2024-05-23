package com.potless.backend.wearable.service;

import com.potless.backend.damage.repository.DamageRepository;
import com.potless.backend.flutter.dto.service.response.DamageAppResponseDTO;
import com.potless.backend.wearable.dto.request.WearableRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WearableService {

    private final DamageRepository damageRepository;

    public boolean getDamage(String h3Index) {
        List<DamageAppResponseDTO> byHexagonIndex = damageRepository.findByHexagonIndex(h3Index);
        return !byHexagonIndex.isEmpty();
    }
}
