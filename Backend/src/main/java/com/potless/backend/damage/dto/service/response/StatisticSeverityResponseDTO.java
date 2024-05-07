package com.potless.backend.damage.dto.service.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "구별 Damage 심각도 통계 조회 DTO")
public class StatisticSeverityResponseDTO {
    
    @Schema(description = "지역 구")
    public String areaGu;

    @Schema(description = "심각도 3 개수")
    public Long severityCount;

    @Builder
    public StatisticSeverityResponseDTO(String areaGu, Long severityCount) {
        this.areaGu = areaGu;
        this.severityCount = severityCount;
    }
}
