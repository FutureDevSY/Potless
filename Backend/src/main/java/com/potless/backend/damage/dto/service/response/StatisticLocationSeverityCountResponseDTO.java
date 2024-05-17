package com.potless.backend.damage.dto.service.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "동별 Damage 통계 조회 DTO")
public class StatisticLocationSeverityCountResponseDTO {

    @Schema(description = "동 이름")
    private String locationName;
    @Schema(description = "작업 전 Damage 수")
    private Long countDamageBefore;
    @Schema(description = "작업 중 Damage 수")
    private Long countDamageDuring;
    @Schema(description = "작업 후 Damage 수")
    private Long countDamageDone;
    @Schema(description = "심각도 1인 Damage 수")
    private Long severityOne;
    @Schema(description = "심각도 2인 Damage 수")
    private Long severityTwo;
    @Schema(description = "심각도 3인 Damage 수")
    private Long severityThree;

    @Builder
    public StatisticLocationSeverityCountResponseDTO(String locationName, Long countDamageBefore, Long countDamageDuring, Long countDamageDone, Long severityOne, Long severityTwo, Long severityThree) {
        this.locationName = locationName;
        this.countDamageBefore = countDamageBefore;
        this.countDamageDuring = countDamageDuring;
        this.countDamageDone = countDamageDone;
        this.severityOne = severityOne;
        this.severityTwo = severityTwo;
        this.severityThree = severityThree;
    }
}
