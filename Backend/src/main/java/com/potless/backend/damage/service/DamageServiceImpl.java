package com.potless.backend.damage.service;

import com.potless.backend.damage.dto.controller.request.DamageSearchRequestDTO;
import com.potless.backend.damage.dto.controller.response.DamageResponseDTO;
import com.potless.backend.damage.dto.controller.response.ImagesResponseDTO;
import com.potless.backend.damage.dto.service.response.KakaoMapApiResponseDTO;
import com.potless.backend.damage.entity.area.AreaEntity;
import com.potless.backend.damage.entity.area.LocationEntity;
import com.potless.backend.damage.entity.road.ImageEntity;
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
        responseDTO.setImagesResponseDTOS(images);  // 오류 부분: 이 부분을 imagesResponseDTOS 로 변경해야 합니다.
        return responseDTO;
    }


    @Override
    @Transactional
    public void setDamage(KakaoMapApiResponseDTO data) {
        AreaEntity areaGu = areaRepository.findByAreaGu(data.getDocuments().get(0).getAddress().getRegion_2depth_name())
                .orElseThrow(PotholeLocationNotFoundException::new);

        LocationEntity locationName = locationRepository.findByLocationName(data.getDocuments().get(0).getAddress().getRegion_3depth_name())
                .orElseThrow(PotholeLocationNotFoundException::new);

        log.info("areaGu = {}", areaGu);
        log.info("locationName = {}", locationName);
    }
}
