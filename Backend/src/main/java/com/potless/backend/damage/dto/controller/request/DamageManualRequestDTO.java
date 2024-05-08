package com.potless.backend.damage.dto.controller.request;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DamageManualRequestDTO {

    private String address;

    private String type;

    @Builder
    public DamageManualRequestDTO(String address, String type) {
        this.address = address;
        this.type = type;
    }
}
