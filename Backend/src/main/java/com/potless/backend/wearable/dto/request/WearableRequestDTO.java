package com.potless.backend.wearable.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WearableRequestDTO {

    private Long x;
    private Long y;

    @Builder
    public WearableRequestDTO(Long x, Long y) {
        this.x = x;
        this.y = y;
    }
}
