package com.potless.backend.damage.dto.service.response.count;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "구별 Damage 발생 통계 조회 리스트 DTO")
public class AreaDamageCountForMonthListResponseDTO {

    List<AreaDamageCountForMonthResponseDTO> list = new ArrayList<>();
    @Schema(description = "구 이름")
    private String areaGu;

    @Builder
    public AreaDamageCountForMonthListResponseDTO(String areaGu, List<AreaDamageCountForMonthResponseDTO> list) {
        this.areaGu = areaGu;
        this.list = list;
    }

}
