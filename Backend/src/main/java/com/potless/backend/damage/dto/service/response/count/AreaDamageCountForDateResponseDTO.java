package com.potless.backend.damage.dto.service.response.count;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "구별 Damage 발생 통계 조회 DTO")
public class AreaDamageCountForDateResponseDTO {

    @Schema(description = "날짜")
    private LocalDate date;

    @Schema(description = "발생 수")
    private Long count;

    @Builder
    public AreaDamageCountForDateResponseDTO(LocalDate date, Long count) {
        this.date = date;
        this.count = count;
    }
}
