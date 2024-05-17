package com.potless.backend.damage.dto.controller.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LocationResponseDTO {

    private Long locationId;

    private String locationName;

    @Builder
    public LocationResponseDTO(Long locationId, String locationName) {
        this.locationId = locationId;
        this.locationName = locationName;
    }
}
