package com.potless.backend.damage.service;

import com.potless.backend.damage.dto.controller.request.DamageSetRequestDTO;
import com.potless.backend.damage.entity.road.DamageEntity;
import com.potless.backend.damage.repository.DamageRepository;
import com.potless.backend.global.exception.pothole.DuplPotholeException;
import com.potless.backend.hexagon.entity.HexagonEntity;
import com.potless.backend.damage.repository.DamageRepository;
import com.potless.backend.global.exception.pothole.DuplPotholeException;
import com.potless.backend.hexagon.repository.HexagonRepository;
import com.potless.backend.hexagon.service.H3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import java.util.Objects;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class DuplicateAreaService {

    private final H3Service h3Service;
    private final DamageRepository damageRepository;
    private final HexagonRepository hexagonRepository;
    private final AsyncService asyncService;
    private final Lock lock = new ReentrantLock();

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public String checkIsDuplicated(DamageSetRequestDTO damageSetRequestDTO) {
        int res = 13;
        String hexagonIndex = h3Service.getH3Index(damageSetRequestDTO.getY(), damageSetRequestDTO.getX(), res);
        hexagonRepository.findByHexagonIndex(hexagonIndex);

        Optional<DamageEntity> optionalDamageEntity =
                damageRepository.findDamageByHexagonIndexAndDtype(hexagonIndex, damageSetRequestDTO.getDtype());

        if (optionalDamageEntity.isPresent()) {
            DamageEntity damageEntity = optionalDamageEntity.get();
            if (!Objects.equals(damageEntity.getDirX(), damageSetRequestDTO.getX())
                    && !Objects.equals(damageEntity.getDirY(), damageSetRequestDTO.getY())) {

                damageEntity.addCount();
                damageRepository.save(damageEntity);
            }
            log.error("Duplicate pothole detected: {}", damageSetRequestDTO);
            throw new DuplPotholeException();
        }
        return hexagonIndex;
    }

//    @Transactional
//    public void checkIsDuplicated(DamageSetRequestDTO damageSetRequestDTO, File imageFile) throws IOException {
//        int res = 13;
//        String hexagonIndex = h3Service.getH3Index(damageSetRequestDTO.getY(), damageSetRequestDTO.getX(), res);
//        HexagonEntity hexagonEntity = hexagonRepository.findByHexagonIndex(hexagonIndex);
//
//        Optional<DamageEntity> optionalDamageEntity = damageRepository.findDamageByHexagonIndexAndDtype(hexagonEntity.getHexagonIndex(), damageSetRequestDTO.getDtype());
//
//        if (optionalDamageEntity.isPresent()) {
//            DamageEntity damageEntity = optionalDamageEntity.get();
//            if (!Objects.equals(damageEntity.getDirX(), damageSetRequestDTO.getX()) && !Objects.equals(damageEntity.getDirY(), damageSetRequestDTO.getY())) {
//                damageEntity.addCount();
//                damageRepository.save(damageEntity);
//            }
//            throw new DuplPotholeException();
//        }
//
//        // 비동기 작업을 트랜잭션 내에서 실행하기 위해 TransactionSynchronizationManager를 사용
//        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
//            @Override
//            public void afterCommit() {
//                CompletableFuture.runAsync(() -> {
//                    try {
//                        asyncService.setDamageAsyncMethod(damageSetRequestDTO, imageFile, hexagonEntity.getHexagonIndex()).get();
//                    } catch (Exception e) {
//                        throw new RuntimeException(e);
//                    }
//                });
//            }
//        });


}
