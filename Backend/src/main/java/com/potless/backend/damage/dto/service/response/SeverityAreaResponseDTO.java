package com.potless.backend.damage.dto.service.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "구 Damage 심각도 통계 조회 DTO")
public class SeverityAreaResponseDTO {

    @Schema(description = "심각도 1인 Damage 수")
    private Long severityOne;
    @Schema(description = "심각도 2인 Damage 수")
    private Long severityTwo;
    @Schema(description = "심각도 3인 Damage 수")
    private Long severityThree;

    @Builder
    public SeverityAreaResponseDTO(Long severityOne, Long severityTwo, Long severityThree) {
        this.severityOne = severityOne;
        this.severityTwo = severityTwo;
        this.severityThree = severityThree;
    }
}
