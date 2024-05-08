package com.potless.backend.damage.service;

import com.potless.backend.damage.dto.controller.response.AreaResponseDTO;
import com.potless.backend.damage.dto.controller.response.LocationResponseDTO;
import com.potless.backend.damage.repository.AreaRepository;
import com.potless.backend.damage.repository.LocationRepository;
import com.potless.backend.global.exception.areaLocation.AreaNotFoundException;
import com.potless.backend.global.exception.areaLocation.LocationNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AreaLocationServiceImpl implements IAreaLocationService {

    private final AreaRepository areaRepository;
    private final LocationRepository locationRepository;

    @Override
    public List<AreaResponseDTO> getAreaList() {
        return areaRepository.findAll().stream()
                .map(areaEntity -> AreaResponseDTO.builder()
                        .areaGu(areaEntity.getAreaGu())
                        .areaId(areaEntity.getId())
                        .build())
                .toList();
    }

    @Override
    public List<LocationResponseDTO> getLocationList() {
        return locationRepository.findAll().stream()
                .map(locationEntity -> LocationResponseDTO.builder()
                        .locationId(locationEntity.getId())
                        .locationName(locationEntity.getLocationName())
                        .build())
                .toList();
    }

    @Override
    public AreaResponseDTO getAreaById(Long areaId) {
        return areaRepository.findById(areaId)
                .map(areaEntity -> AreaResponseDTO.builder()
                        .areaId(areaEntity.getId())
                        .areaGu(areaEntity.getAreaGu())
                        .build())
                .orElseThrow(AreaNotFoundException::new);
    }

    @Override
    public LocationResponseDTO getLocationById(Long locationId) {
        return locationRepository.findById(locationId)
                .map(locationEntity -> LocationResponseDTO.builder()
                        .locationId(locationEntity.getId())
                        .locationName(locationEntity.getLocationName())
                        .build())
                .orElseThrow(LocationNotFoundException::new);
    }
}
