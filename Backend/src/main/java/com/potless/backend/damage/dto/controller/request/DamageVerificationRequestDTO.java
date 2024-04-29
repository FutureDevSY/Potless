package com.potless.backend.damage.dto.controller.request;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DamageVerificationRequestDTO {

    private String dtype;

    private String damageAddress;

    private String damageRoadName;

    private String area;

    private String location;

    @Builder
    public DamageVerificationRequestDTO(String dtype, String damageAddress, String damageRoadName, String area, String location) {
        this.dtype = dtype;
        this.damageAddress = damageAddress;
        this.damageRoadName = damageRoadName;
        this.area = area;
        this.location = location;
    }
}
