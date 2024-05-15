package com.potless.backend.damage.service;

import com.potless.backend.damage.dto.controller.request.DamageSetRequestDTO;
<<<<<<< HEAD
=======
import com.potless.backend.damage.entity.road.DamageEntity;
>>>>>>> dev-BE
import com.potless.backend.damage.repository.DamageRepository;
import com.potless.backend.global.exception.pothole.DuplPotholeException;
import com.potless.backend.hexagon.service.H3Service;
import lombok.RequiredArgsConstructor;
<<<<<<< HEAD
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
@EnableAsync
@RequiredArgsConstructor
@Transactional
=======
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
>>>>>>> dev-BE
public class DuplicateAreaService {

    private final H3Service h3Service;
    private final DamageRepository damageRepository;

<<<<<<< HEAD
    public String checkIsDuplicated(DamageSetRequestDTO damageSetRequestDTO){
        //중복처리
        int res = 13;
        String hexagonIndex = h3Service.getH3Index(damageSetRequestDTO.getY(), damageSetRequestDTO.getX(), res);
        if (damageRepository.findDamageByHexagonIndexAndDtype(hexagonIndex, damageSetRequestDTO.getDtype())) {
=======
    public String checkIsDuplicated(DamageSetRequestDTO damageSetRequestDTO) {
        int res = 13;
        String hexagonIndex = h3Service.getH3Index(damageSetRequestDTO.getY(), damageSetRequestDTO.getX(), res);
        Optional<DamageEntity> optionalDamageEntity = damageRepository.findDamageByHexagonIndexAndDtype(hexagonIndex, damageSetRequestDTO.getDtype());

        if (optionalDamageEntity.isPresent()) {
            DamageEntity damageEntity = optionalDamageEntity.get();
            if (!Objects.equals(damageEntity.getDirX(), damageSetRequestDTO.getX()) && !Objects.equals(damageEntity.getDirY(), damageSetRequestDTO.getY())) {
                damageEntity.addCount();
                damageRepository.save(damageEntity);
            }
>>>>>>> dev-BE
            throw new DuplPotholeException();
        }
        return hexagonIndex;
    }
}
