package com.potless.backend.damage.dto.controller.request;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DamageVerificationRequestDTO {

    private String dtype;

    private String damageAddress;

    private String area;

    private String location;

    private List<String> images = new ArrayList<>();

    @Builder
    public DamageVerificationRequestDTO(String dtype, String damageAddress, String area, String location, List<String> images) {
        this.dtype = dtype;
        this.damageAddress = damageAddress;
        this.area = area;
        this.location = location;
        this.images = images;
    }
}
