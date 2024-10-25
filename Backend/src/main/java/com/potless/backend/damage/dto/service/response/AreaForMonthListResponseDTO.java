package com.potless.backend.damage.dto.service.response;

import com.potless.backend.damage.dto.service.response.count.AreaDamageCountForMonthListResponseDTO;
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
public class AreaForMonthListResponseDTO {

    private List<AreaDamageCountForMonthListResponseDTO> list = new ArrayList<>();

    @Builder
    public AreaForMonthListResponseDTO(List<AreaDamageCountForMonthListResponseDTO> list) {
        this.list = list;
    }
}
