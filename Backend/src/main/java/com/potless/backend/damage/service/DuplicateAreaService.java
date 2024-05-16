package com.potless.backend.damage.service;

import com.potless.backend.damage.dto.controller.request.DamageSetRequestDTO;
import com.potless.backend.damage.entity.road.DamageEntity;
import com.potless.backend.damage.event.DamageAsyncEvent;
import com.potless.backend.damage.repository.DamageRepository;
import com.potless.backend.global.exception.pothole.DuplPotholeException;
import com.potless.backend.hexagon.entity.HexagonEntity;
import com.potless.backend.hexagon.repository.HexagonRepository;
import com.potless.backend.hexagon.service.H3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.transaction.support.TransactionTemplate;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DuplicateAreaService {

    private final H3Service h3Service;
    private final DamageRepository damageRepository;
    private final HexagonRepository hexagonRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void checkIsDuplicated(DamageSetRequestDTO damageSetRequestDTO, File imageFile) throws IOException {
        int res = 13;
        String hexagonIndex = h3Service.getH3Index(damageSetRequestDTO.getY(), damageSetRequestDTO.getX(), res);
        HexagonEntity hexagonEntity = hexagonRepository.findByHexagonIndex(hexagonIndex);

        Optional<DamageEntity> optionalDamageEntity = damageRepository.findDamageByHexagonIndexAndDtype(hexagonEntity.getHexagonIndex(), damageSetRequestDTO.getDtype());

        if (optionalDamageEntity.isPresent()) {
            DamageEntity damageEntity = optionalDamageEntity.get();
            if (!Objects.equals(damageEntity.getDirX(), damageSetRequestDTO.getX()) && !Objects.equals(damageEntity.getDirY(), damageSetRequestDTO.getY())) {
                damageEntity.addCount();
                damageRepository.save(damageEntity);
            }
            throw new DuplPotholeException();
        }

        // 클라이언트에 빠르게 응답을 반환하기 위해 이벤트를 발행
        eventPublisher.publishEvent(new DamageAsyncEvent(damageSetRequestDTO, imageFile, hexagonEntity.getHexagonIndex()));
    }
}
