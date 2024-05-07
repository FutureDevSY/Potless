package com.potless.backend.damage.dto.service.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "구별 Damage 통계 조회 DTO")
public class StatisticCountResponseDTO {

    @Schema(description = "지역 구")
    private String areaGu;
    @Schema(description = "작업 전 Damage 수")
    private Long countDamageBefore;
    @Schema(description = "작업 중 Damage 수")
    private Long countDamageDuring;
    @Schema(description = "작업 후 Damage 수")
    private Long countDamageDone;

    @Builder
    public StatisticCountResponseDTO(String areaGu, Long countDamageBefore, Long countDamageDuring, Long countDamageDone) {
        this.areaGu = areaGu;
        this.countDamageBefore = countDamageBefore;
        this.countDamageDuring = countDamageDuring;
        this.countDamageDone = countDamageDone;
    }
}
