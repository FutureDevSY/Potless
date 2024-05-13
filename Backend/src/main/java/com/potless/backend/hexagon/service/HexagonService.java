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
    private final HexagonRepository hexagonRepository;
    private final DamageRepository damageRepository;

    public boolean duplCheck(HexagonRequestDto duplRequestDto) {

        Long hexagonId = getHexagon(duplRequestDto.getLatitude(),duplRequestDto.getLongitude());

        List<DamageEntity> damageEntities = damageRepository.findDamageByHexagonIdAndDtype(hexagonId, duplRequestDto.getType());

        return damageEntities.isEmpty();
    }

    public Long getHexagon(Double latitude, Double longitude) {
        return h3.latLngToCell(latitude, longitude, 8);
    }


}
