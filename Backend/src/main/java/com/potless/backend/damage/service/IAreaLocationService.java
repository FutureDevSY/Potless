package com.potless.backend.damage.service;

import com.potless.backend.damage.dto.controller.response.AreaResponseDTO;
import com.potless.backend.damage.dto.controller.response.LocationResponseDTO;

import java.util.List;

public interface IAreaLocationService {
    List<AreaResponseDTO> getAreaList();

    List<LocationResponseDTO> getLocationList();

    AreaResponseDTO getAreaById(Long areaId);

    LocationResponseDTO getLocationById(Long locationId);
}
