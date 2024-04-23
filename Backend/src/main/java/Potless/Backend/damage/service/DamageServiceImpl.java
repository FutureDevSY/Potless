package Potless.Backend.damage.service;

import Potless.Backend.damage.dto.controller.request.DamageSearchRequestDTO;
import Potless.Backend.damage.dto.controller.response.DamageResponseDTO;
import Potless.Backend.damage.dto.service.response.KakaoMapApiResponseDTO;
import Potless.Backend.damage.entity.area.AreaEntity;
import Potless.Backend.damage.entity.area.LocationEntity;
import Potless.Backend.damage.repository.AreaRepository;
import Potless.Backend.damage.repository.DamageRepository;
import Potless.Backend.damage.repository.LocationRepository;
import Potless.Backend.global.exception.pothole.PotholeAreaNotFoundException;
import Potless.Backend.global.exception.pothole.PotholeLocationNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DamageServiceImpl implements IDamageService {

    private final DamageRepository damageRepository;
    private final AreaRepository areaRepository;
    private final LocationRepository locationRepository;

    @Override
    public Page<DamageResponseDTO> getDamages(DamageSearchRequestDTO damageSearchRequestDTO, Pageable pageable) {
        return damageRepository.findDamagesWithLatestTransaction(damageSearchRequestDTO, pageable);
    }

    @Override
    public DamageResponseDTO getDamage(Long damageId) {
        return damageRepository.findDamageDetailsById(damageId);
    }


    @Override
    @Transactional
    public void setDamage(KakaoMapApiResponseDTO data) {
        AreaEntity areaGu = areaRepository.findByAreaGu(data.getDocuments().get(0).getAddress().getRegion_2depth_name())
                .orElseThrow(PotholeAreaNotFoundException::new);

        LocationEntity locationName = locationRepository.findByLocationName(data.getDocuments().get(0).getAddress().getRegion_3depth_name())
                .orElseThrow(PotholeLocationNotFoundException::new);

        log.info("areaGu = {}", areaGu);
        log.info("locationName = {}", locationName);


    }
}
