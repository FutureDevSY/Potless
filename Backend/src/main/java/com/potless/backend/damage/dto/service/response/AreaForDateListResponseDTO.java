package com.potless.backend.damage.dto.service.response;

import com.potless.backend.damage.dto.service.response.count.AreaDamageCountForDateListResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "구별 Damage 발생 통계 조회 리스트 DTO")
public class AreaForDateListResponseDTO {

    private List<AreaDamageCountForDateListResponseDTO> list = new ArrayList<>();

    @Builder
    public AreaForDateListResponseDTO(List<AreaDamageCountForDateListResponseDTO> list) {
        this.list = list;
    }
}
