package com.potless.backend.damage.dto.service.response.count;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.YearMonth;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "구별 Damage 발생 통계 조회 DTO")
public class AreaDamageCountForMonthResponseDTO {

    @Schema(description = "월")
    private YearMonth month;

    @Schema(description = "발생 수")
    private Long count;

    @Builder
    public AreaDamageCountForMonthResponseDTO(YearMonth month, Long count) {
        this.month = month;
        this.count = count;
    }
}
