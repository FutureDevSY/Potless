package com.potless.backend.damage.service;

import com.potless.backend.damage.dto.controller.request.DamageSetRequestDTO;
import com.potless.backend.damage.repository.DamageRepository;
import com.potless.backend.global.exception.pothole.DuplPotholeException;
import com.potless.backend.hexagon.service.H3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
@EnableAsync
@RequiredArgsConstructor
@Transactional
public class DuplicateAreaService {

    private final H3Service h3Service;
    private final DamageRepository damageRepository;

    public String checkIsDuplicated(DamageSetRequestDTO damageSetRequestDTO){
        //중복처리
        int res = 13;
        String hexagonIndex = h3Service.getH3Index(damageSetRequestDTO.getY(), damageSetRequestDTO.getX(), res);
        if (damageRepository.findDamageByHexagonIndexAndDtype(hexagonIndex, damageSetRequestDTO.getDtype())) {
            throw new DuplPotholeException();
        }
        return hexagonIndex;
    }
}
