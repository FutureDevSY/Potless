package com.potless.backend.damage.service;

import com.potless.backend.damage.repository.DamageRepository;
import com.potless.backend.hexagon.repository.HexagonRepository;
import com.potless.backend.hexagon.service.H3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class DuplicateAreaService {

    private final H3Service h3Service;
    private final DamageRepository damageRepository;
    private final HexagonRepository hexagonRepository;

//    public String checkIsDuplicated(DamageSetRequestDTO damageSetRequestDTO) {
//        int res = 13;
//        String hexagonIndex = h3Service.getH3Index(damageSetRequestDTO.getY(), damageSetRequestDTO.getX(), res);
//        HexagonEntity hexagonEntity = hexagonRepository.findByHexagonIndex(hexagonIndex);
//        Optional<DamageEntity> optionalDamageEntity = damageRepository.findDamageByHexagonIndexAndDtype(hexagonIndex, damageSetRequestDTO.getDtype());
//
//        if (optionalDamageEntity.isPresent()) {
//            DamageEntity damageEntity = optionalDamageEntity.get();
//            if (!Objects.equals(damageEntity.getDirX(), damageSetRequestDTO.getX()) && !Objects.equals(damageEntity.getDirY(), damageSetRequestDTO.getY())) {
//                damageEntity.addCount();
//                damageRepository.save(damageEntity);
//            }
//            throw new DuplPotholeException();
//        }
//        return hexagonIndex;
//    }
}
