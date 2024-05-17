package com.potless.backend.damage.dto.controller.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DamageDeleteRequestDTO {

    @NotEmpty(message = "삭제할 Damage 를 최소 1개 이상 등록 해 주세요")
    private List<Long> damageIds = new ArrayList<>();

    @Builder
    public DamageDeleteRequestDTO(List<Long> damageIds) {
        this.damageIds = damageIds;
    }
}
