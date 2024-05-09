package com.potless.backend.damage.dto.controller.request;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DamageManualRequestDTO {

    @NotNull(message = "주소는 비어있을 수 없습니다")
    private String address;

    @NotNull(message = "심각도는 비어있을 수 없습니다")
    private Integer severity;

    @NotNull(message = "타입은 비어있을 수 없습니다")
    private String type;

    @Builder
    public DamageManualRequestDTO(String address, Integer severity, String type) {
        this.address = address;
        this.severity = severity;
        this.type = type;
    }
}
