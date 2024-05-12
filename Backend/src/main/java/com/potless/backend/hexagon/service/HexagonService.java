package com.potless.backend.hexagon.service;

import com.potless.backend.damage.entity.road.DamageEntity;
import com.potless.backend.damage.repository.DamageRepository;
import com.potless.backend.global.exception.hexagon.HexagonNotFoundException;
import com.potless.backend.hexagon.dto.requestDto.HexagonRequestDto;
import com.potless.backend.hexagon.entity.HexagonEntity;
import com.potless.backend.hexagon.repository.HexagonRepository;
import com.uber.h3core.H3Core;
import com.uber.h3core.util.LatLng;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class HexagonService {
    private final H3Core h3;
    private final DamageRepository damageRepository;

    public boolean duplCheck(Double x, Double y, String dtype) {
        int res = 13;

        Long hexagonId = h3.latLngToCell(x, y, res);

        List<DamageEntity> damageEntities = damageRepository.findDamageByHexagonIdAndDtype(hexagonId, dtype);

        return !damageEntities.isEmpty();
    }
}
