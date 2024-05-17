package com.potless.backend.damage.service;

import com.potless.backend.damage.dto.controller.request.AreaDamageCountForDateRequestDTO;
import com.potless.backend.damage.dto.controller.request.DamageSearchRequestDTO;
import com.potless.backend.damage.dto.controller.response.DamageResponseDTO;
import com.potless.backend.damage.dto.controller.response.ImagesResponseDTO;
import com.potless.backend.damage.dto.service.request.AreaDamageCountForMonthServiceRequestDTO;
import com.potless.backend.damage.dto.service.request.DamageSetRequestServiceDTO;
import com.potless.backend.damage.dto.service.response.*;
import com.potless.backend.damage.entity.area.AreaEntity;
import com.potless.backend.damage.entity.area.LocationEntity;
import com.potless.backend.damage.entity.enums.Status;
import com.potless.backend.damage.entity.road.*;
import com.potless.backend.damage.repository.AreaRepository;
import com.potless.backend.damage.repository.DamageRepository;
import com.potless.backend.damage.repository.ImageRepository;
import com.potless.backend.damage.repository.LocationRepository;
import com.potless.backend.global.exception.member.MemberNotFoundException;
import com.potless.backend.global.exception.pothole.PotholeLocationNotFoundException;
import com.potless.backend.global.exception.pothole.PotholeNotFoundException;
import com.potless.backend.hexagon.entity.HexagonEntity;
import com.potless.backend.hexagon.repository.HexagonRepository;
import com.potless.backend.member.entity.MemberEntity;
import com.potless.backend.member.repository.member.MemberRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    private final HexagonRepository hexagonRepository;
    private final MemberRepository memberRepository;
    private final EntityManager entityManager;  // EntityManager 주입

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

        HexagonEntity hexagonEntity = hexagonRepository.findByHexagonIndex(data.getHexagonIndex());

        MemberEntity member = memberRepository.findById(data.getMemberId())
                .orElseThrow(MemberNotFoundException::new);

        DamageEntity damageEntity;
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
                    .hexagonEntity(hexagonEntity)
                    .memberEntity(member)
                    .build();
        } else if (data.getDtype().equals("POTHOLE")) {
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
                    .hexagonEntity(hexagonEntity)
                    .memberEntity(member)
                    .build();
        } else {
            damageEntity = WornOutEntity.builder()
                    .dirX(data.getDirX())
                    .dirY(data.getDirY())
                    .address(data.getAddress())
                    .dtype(data.getDtype())
                    .status(data.getStatus())
                    .areaEntity(areaGu)
                    .locationEntity(locationName)
                    .width(data.getWidth())
                    .severity(data.getSeverity())
                    .hexagonEntity(hexagonEntity)
                    .memberEntity(member)
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
    @Transactional
    public List<String> deleteDamage(List<Long> damageIds) {
        // 여러 Damage 엔티티 조회
        List<DamageEntity> damages = damageRepository.findAllById(damageIds);

        // S3에서 삭제할 파일 URL 수집
        List<String> urlsToDelete = damages.stream()
                .flatMap(damage -> damage.getImageEntities().stream())
                .map(ImageEntity::getUrl)
                .filter(url -> !url.equals("https://mine702-amazon-s3.s3.ap-northeast-2.amazonaws.com/Default/default.jpg"))
                .toList();

        // Damage 엔티티 삭제
        damageRepository.deleteAll(damages);

        return urlsToDelete;
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
    public List<String> setChangeImage(Long damageId, List<String> fileUrls) {
        DamageEntity damageEntity = damageRepository.findById(damageId).orElseThrow(PotholeNotFoundException::new);

        if (damageEntity.getStatus().equals(Status.작업전)) {
            List<String> urlsToDelete = damageEntity.getImageEntities().stream()
                    .map(ImageEntity::getUrl)
                    .filter(url -> !url.equals("https://mine702-amazon-s3.s3.ap-northeast-2.amazonaws.com/Default/default.jpg"))
                    .toList();

            List<Long> imageIds = damageEntity.getImageEntities().stream()
                    .map(ImageEntity::getId)
                    .toList();

            imageRepository.deleteByIds(imageIds);

            int order = 1;
            for (String imageUrl : fileUrls) {
                ImageEntity image = ImageEntity.builder()
                        .damageEntity(damageEntity)
                        .url(imageUrl)
                        .order(order++)
                        .build();
                imageRepository.save(image);
            }
            return urlsToDelete;
        } else {
            return null;
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

    @Override
    @Transactional
    public void setAsyncDamage(DamageSetRequestServiceDTO serviceDTO) {
        AreaEntity areaGu = areaRepository.findByAreaGu(serviceDTO.getArea())
                .orElseThrow(PotholeLocationNotFoundException::new);

        LocationEntity locationName = locationRepository.findByLocationName(serviceDTO.getLocation())
                .orElseThrow(PotholeLocationNotFoundException::new);

        HexagonEntity hexagonEntity = hexagonRepository.findByHexagonIndex(serviceDTO.getHexagonIndex());

        MemberEntity member = memberRepository.findById(serviceDTO.getMemberId())
                .orElseThrow(MemberNotFoundException::new);

        LocalDateTime now = LocalDateTime.now();

        damageRepository.insertIfNotExistsWithLock(
                serviceDTO.getSeverity(),
                serviceDTO.getDirX(),
                serviceDTO.getDirY(),
                serviceDTO.getAddress(),
                serviceDTO.getWidth(),
                serviceDTO.getStatus().name(),
                areaGu.getId(),
                locationName.getId(),
                hexagonEntity.getId(),
                serviceDTO.getDtype(),
                member.getId(),
                now,
                now
        );

        DamageEntity damageEntity = damageRepository.findTopByHexagonEntityAndDtypeOrderByCreatedDateTimeDesc(hexagonEntity, serviceDTO.getDtype());

        if (damageEntity.getImageEntities().isEmpty()) {
            int order = 1;
            for (String imageUrl : serviceDTO.getImages()) {
                ImageEntity image = ImageEntity.builder()
                        .damageEntity(damageEntity)
                        .url(imageUrl)
                        .order(order++)
                        .build();
                imageRepository.save(image);
            }
        }
    }

}
