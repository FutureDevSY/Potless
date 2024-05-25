package com.potless.backend.wearable.dto.request;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WearableRequestDTO {

    private Double x;
    private Double y;

    @Builder
    public WearableRequestDTO(Double x, Double y) {
        this.x = x;
        this.y = y;
    }
}
