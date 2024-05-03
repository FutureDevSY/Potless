package com.potless.backend.damage.dto.controller.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AreaDamageCountForDateRequestDTO {

    @NotNull(message = "시작 값은 비어있을 수 없습니다")
    private LocalDate start;
    private LocalDate end;

    @Builder
    public AreaDamageCountForDateRequestDTO(LocalDate start, LocalDate end) {
        this.start = start;
        this.end = end;
    }
}
