package com.potless.backend.damage.dto.controller.response;

import lombok.*;

@Getter
@ToString
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
