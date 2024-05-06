package com.potless.backend.damage.dto.controller.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AreaDamageCountForMonthRequestDTO {

    @NotNull(message = "시작 월은 비어있을 수 없습니다")
    private String start;
    private String end;

    @Builder
    public AreaDamageCountForMonthRequestDTO(String start, String end) {
        this.start = start;
        this.end = end;
    }
}
