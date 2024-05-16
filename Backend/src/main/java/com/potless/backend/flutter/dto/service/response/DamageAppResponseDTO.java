package com.potless.backend.flutter.dto.service.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DamageAppResponseDTO {

    private Integer severity;
    private Double dirX;
    private Double dirY;
    private String address;
    private String dtype;

    @Builder
    @QueryProjection
    public DamageAppResponseDTO(Integer severity, Double dirX, Double dirY, String address, String dtype) {
        this.severity = severity;
        this.dirX = dirX;
        this.dirY = dirY;
        this.address = address;
        this.dtype = dtype;
    }
}
