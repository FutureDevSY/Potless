package com.potless.backend.damage.dto.service.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "단일 구의 동별 Damage 통계 조회 DTO")
public class StatisticListResponseDTO {

    @Schema(description = "지역 구")
    public String areaGu;
    @Schema(description = "동별 통계 DTO")
    public List<StatisticLocationSeverityCountResponseDTO> list = new ArrayList<>();

    @Builder
    public StatisticListResponseDTO(String areaGu, List<StatisticLocationSeverityCountResponseDTO> list) {
        this.areaGu = areaGu;
        this.list = list;
    }

}
