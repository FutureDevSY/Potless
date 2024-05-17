package com.potless.backend.damage.dto.controller.request;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DamageDoneRequestDTO {

    @NotNull(message = "데미지 ID 는 비어있을 수 없습니다")
    private Long damageId;

    @Builder
    public DamageDoneRequestDTO(Long damageId) {
        this.damageId = damageId;
    }
}
