package com.potless.backend.damage.dto.service.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "동별 Damage 통계 조회 DTO")
public class StatisticLocationCountResponseDTO {

    @Schema(description = "동 이름")
    private String locationName;
    @Schema(description = "작업 전 Damage 수")
    private Long countDamageBefore;
    @Schema(description = "작업 중 Damage 수")
    private Long countDamageDuring;
    @Schema(description = "작업 후 Damage 수")
    private Long countDamageDone;

    @Builder
    public StatisticLocationCountResponseDTO(String locationName, Long countDamageBefore, Long countDamageDuring, Long countDamageDone) {
        this.locationName = locationName;
        this.countDamageBefore = countDamageBefore;
        this.countDamageDuring = countDamageDuring;
        this.countDamageDone = countDamageDone;
    }
}
