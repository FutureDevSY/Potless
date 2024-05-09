package com.potless.backend.damage.dto.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DamageDoneRequestDTO {

    @NotBlank(message = "데미지 ID 는 비어있을 수 없습니다")
    private Long damageId;

    @Builder
    public DamageDoneRequestDTO(Long damageId) {
        this.damageId = damageId;
    }
}
