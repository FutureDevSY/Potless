package com.potless.backend.damage.dto.controller.request;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DamageManualRequestDTO {

    @NotNull(message = "x 값을 입력 해 주세요")
    private Double x;

    @NotNull(message = "y 값을 입력 해 주세요")
    private Double y;

    @NotNull(message = "심각도는 비어있을 수 없습니다")
    private Integer severity;

    @NotNull(message = "타입은 비어있을 수 없습니다")
    private String type;

    @Builder

    public DamageManualRequestDTO(Double x, Double y, Integer severity, String type) {
        this.x = x;
        this.y = y;
        this.severity = severity;
        this.type = type;
    }
}

