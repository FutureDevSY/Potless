package com.potless.backend.damage.service;

import com.potless.backend.damage.dto.controller.request.AreaDamageCountForDateRequestDTO;
import com.potless.backend.damage.dto.controller.request.DamageSearchRequestDTO;
import com.potless.backend.damage.dto.controller.request.DamageVerificationRequestDTO;
import com.potless.backend.damage.dto.controller.response.DamageResponseDTO;
import com.potless.backend.damage.dto.controller.response.ImagesResponseDTO;
import com.potless.backend.damage.dto.service.request.AreaDamageCountForMonthServiceRequestDTO;
import com.potless.backend.damage.dto.service.request.DamageSetRequestServiceDTO;
import com.potless.backend.damage.dto.service.response.*;
import com.potless.backend.damage.entity.area.AreaEntity;
import com.potless.backend.damage.entity.area.LocationEntity;
import com.potless.backend.damage.entity.enums.Status;
import com.potless.backend.damage.entity.road.CrackEntity;
import com.potless.backend.damage.entity.road.DamageEntity;
import com.potless.backend.damage.entity.road.ImageEntity;
import com.potless.backend.damage.entity.road.PotholeEntity;
import com.potless.backend.damage.repository.AreaRepository;
import com.potless.backend.damage.repository.DamageRepository;
import com.potless.backend.damage.repository.ImageRepository;
import com.potless.backend.damage.repository.LocationRepository;
import com.potless.backend.global.exception.pothole.PotholeLocationNotFoundException;
import com.potless.backend.global.exception.pothole.PotholeNotFoundException;
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
        log.info("data = {}", data);
        if (data.getDtype().equals("CRACK")) {
            damageEntity = CrackEntity.builder()
                    .dirX(data.getDirX())
                    .dirY(data.getDirY())
                    .address(data.getAddress())
                    .dtype(data.getDtype())
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
                    .status(data.getStatus())
                    .areaEntity(areaGu)
                    .locationEntity(locationName)
                    .width(data.getWidth())
                    .severity(data.getSeverity())
                    .build();
        }
        damageRepository.save(damageEntity);
        int order = 1;
        for (String imageUrl : data.getImages()) {
            ImageEntity image = ImageEntity.builder()
                    .damageEntity(damageEntity)
                    .url(imageUrl)
                    .order(order++)
                    .build();
            imageRepository.save(image);
        }
        areaGu.addCount();
    }

    @Override
    public List<DamageResponseDTO> getDamageVerification(DamageVerificationRequestDTO data) {
        return damageRepository.findDamagesByVerificationRequest(data);
    }

    @Override
    @Transactional
    public void deleteDamage(Long damageId) {
        DamageEntity damageEntity = damageRepository.findById(damageId).orElseThrow(PotholeNotFoundException::new);
        damageEntity.getAreaEntity().minusCount();
        damageRepository.deleteById(damageId);
    }

    @Override
    public StatisticLocationCountResponseDTO getStatisticLocation(String locationName) {
        return damageRepository.getStatisticLocation(locationName);
    }

    @Override
    public List<StatisticLocationCountResponseDTO> getStatisticLocations() {
        return damageRepository.getStatisticLocations();
    }

    @Override
    public StatisticListResponseDTO getStatistic(Long areaId) {
        return damageRepository.getStatistic(areaId);
    }

    @Override
    public List<StatisticCountResponseDTO> getStatistics() {
        return damageRepository.getStatistics();
    }

    @Override
    @Transactional
    public void setImageForStatus(Long damageId, List<String> fileUrls) {
        DamageEntity damageEntity = damageRepository.findById(damageId).orElseThrow(PotholeNotFoundException::new);
        int order = 1;
        for (String imageUrl : fileUrls) {
            ImageEntity image = ImageEntity.builder()
                    .damageEntity(damageEntity)
                    .url(imageUrl)
                    .order(order++)
                    .build();
            imageRepository.save(image);
        }
    }

    @Override
    @Transactional
    public void setWorkDone(Long damageId) {
        DamageEntity damageEntity = damageRepository.findById(damageId).orElseThrow(PotholeNotFoundException::new);
        damageEntity.changeStatus(Status.작업완료);
    }

    @Override
    public AreaForDateListResponseDTO getAreaDamageCountForDate(AreaDamageCountForDateRequestDTO areaDamageCountForDateRequestDTO) {
        return damageRepository.getAreaDamageCountForDate(areaDamageCountForDateRequestDTO);
    }

    @Override
    public AreaForMonthListResponseDTO getAreaDamageCountForMonth(AreaDamageCountForMonthServiceRequestDTO areaDamageCountForMonthServiceRequestDTO) {
        return damageRepository.getAreaDamageCountForMonth(areaDamageCountForMonthServiceRequestDTO);
    }

    //    @Override
//    public List<DamageResponseDTO> getWorkDamage(Long memberId) {
//        return damageRepository.findDamagesByWorker(memberId);
//    }
}
