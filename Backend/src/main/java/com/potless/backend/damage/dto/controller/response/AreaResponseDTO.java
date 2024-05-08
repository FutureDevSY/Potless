package com.potless.backend.damage.dto.controller.response;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AreaResponseDTO {

    private Long areaId;

    private String areaGu;

    @Builder
    public AreaResponseDTO(Long areaId, String areaGu) {
        this.areaId = areaId;
        this.areaGu = areaGu;
    }
}
