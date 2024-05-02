package com.potless.backend.damage.service;

import com.potless.backend.damage.dto.controller.request.DamageSearchRequestDTO;
import com.potless.backend.damage.dto.controller.request.DamageVerificationRequestDTO;
import com.potless.backend.damage.dto.controller.response.DamageResponseDTO;
import com.potless.backend.damage.dto.controller.response.ImagesResponseDTO;
import com.potless.backend.damage.dto.service.request.DamageSetRequestServiceDTO;
import com.potless.backend.damage.entity.area.AreaEntity;
import com.potless.backend.damage.entity.area.LocationEntity;
import com.potless.backend.damage.entity.road.CrackEntity;
import com.potless.backend.damage.entity.road.DamageEntity;
import com.potless.backend.damage.entity.road.ImageEntity;
import com.potless.backend.damage.entity.road.PotholeEntity;
import com.potless.backend.damage.repository.AreaRepository;
import com.potless.backend.damage.repository.DamageRepository;
import com.potless.backend.damage.repository.ImageRepository;
import com.potless.backend.damage.repository.LocationRepository;
import com.potless.backend.global.exception.pothole.PotholeLocationNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DamageServiceImpl implements IDamageService {

    private final DamageRepository damageRepository;
    private final AreaRepository areaRepository;
    private final LocationRepository locationRepository;
    private final ImageRepository imageRepository;

    @Override
    public Page<DamageResponseDTO> getDamages(DamageSearchRequestDTO damageSearchRequestDTO, Pageable pageable) {
        return damageRepository.findDamagesWithLatestTransaction(damageSearchRequestDTO, pageable);
    }

    @Override
    public DamageResponseDTO getDamage(Long damageId) {
        DamageResponseDTO responseDTO = damageRepository.findDamageDetailsByIdSimple(damageId);

        List<ImageEntity> images = imageRepository.findByDamageEntityId(damageId);
        List<ImagesResponseDTO> imagesResponseDTOS = images.stream()
                .map(img -> new ImagesResponseDTO(img.getId(), img.getUrl(), img.getOrder()))
                .toList();

        responseDTO.setImagesResponseDTOS(imagesResponseDTOS);  // 오류 부분: 이 부분을 imagesResponseDTOS 로 변경해야 합니다.
        return responseDTO;
    }


    @Override
    @Transactional
    public void setDamage(DamageSetRequestServiceDTO data) {
        AreaEntity areaGu = areaRepository.findByAreaGu(data.getArea())
                .orElseThrow(PotholeLocationNotFoundException::new);

        LocationEntity locationName = locationRepository.findByLocationName(data.getLocation())
                .orElseThrow(PotholeLocationNotFoundException::new);

        DamageEntity damageEntity;

        if (data.getDtype().equals("CRACK")) {
            damageEntity = CrackEntity.builder()
                    .dirX(data.getDirX())
                    .dirY(data.getDirY())
                    .address(data.getAddress())
                    .dtype(data.getDtype())
                    .roadName(data.getRoadName())
                    .status(data.getStatus())
                    .areaEntity(areaGu)
                    .locationEntity(locationName)
                    .width(data.getWidth())
                    .severity(data.getSeverity())
                    .build();
        } else {
            damageEntity = PotholeEntity.builder()
                    .dirX(data.getDirX())
                    .dirY(data.getDirY())
                    .address(data.getAddress())
                    .dtype(data.getDtype())
                    .roadName(data.getRoadName())
                    .status(data.getStatus())
                    .areaEntity(areaGu)
                    .locationEntity(locationName)
                    .width(data.getWidth())
                    .severity(data.getSeverity())
                    .build();
        }
        damageRepository.save(damageEntity);
        log.info("저장 완료");
        int order = 1;
        for (String imageUrl : data.getImages()) {
            ImageEntity image = ImageEntity.builder()
                    .damageEntity(damageEntity)
                    .url(imageUrl)
                    .order(order++)
                    .build();
            imageRepository.save(image);
        }
    }

    @Override
    public List<DamageResponseDTO> getDamageVerification(DamageVerificationRequestDTO data) {
        return damageRepository.findDamagesByVerificationRequest(data);
    }

    @Override
    public void deleteDamage(Long damageId) {
        damageRepository.deleteById(damageId);
    }

//    @Override
//    public List<DamageResponseDTO> getWorkDamage(Long memberId) {
//        return damageRepository.findDamagesByWorker(memberId);
//    }
}
