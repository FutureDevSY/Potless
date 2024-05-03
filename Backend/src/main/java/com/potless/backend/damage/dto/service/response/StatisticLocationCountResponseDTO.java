package com.potless.backend.damage.dto.service.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "동별 Damage 통계 조회 DTO")
public class StatisticLocationCountResponseDTO {

    @Schema(description = "동 이름")
    private String locationName;
    @Schema(description = "작업 전 Damage 수")
    private Long countDamageBefore;
    @Schema(description = "작업 후 Damage 수")
    private Long countDamageDone;

    @Builder
    public StatisticLocationCountResponseDTO(String locationName, Long countDamageBefore, Long countDamageDone) {
        this.locationName = locationName;
        this.countDamageBefore = countDamageBefore;
        this.countDamageDone = countDamageDone;
    }
}
