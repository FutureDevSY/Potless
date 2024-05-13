package com.potless.backend.hexagon.service;

import com.potless.backend.hexagon.entity.HexagonEntity;
import com.potless.backend.hexagon.repository.HexagonRepository;
import lombok.RequiredArgsConstructor;
<<<<<<< HEAD
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
=======
import org.springframework.stereotype.Service;

>>>>>>> dev-BE
@Service
@RequiredArgsConstructor
public class HexagonService {

    private final HexagonRepository hexagonRepository;
//    private final H3Core h3;
//    private final DamageRepository damageRepository;

//    public boolean duplCheck(Double x, Double y, String dtype) {
//        int res = 13;
//
//        Long hexagonId = h3.latLngToCell(x, y, res);
//
//        List<DamageEntity> damageEntities = damageRepository.findDamageByHexagonIdAndDtype(hexagonId, dtype);
//
//        return !damageEntities.isEmpty();
//    }

    public HexagonEntity getHexagon(String hexagonIndex) {
        return hexagonRepository.findByHexagonIndex(hexagonIndex);
    }
}
