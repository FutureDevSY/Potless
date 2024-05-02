package com.potless.backend.damage.dto.service.response;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StatisticListResponseDTO {

    public String areaGu;
    public List<StatisticLocationCountResponseDTO> list = new ArrayList<>();

    @Builder
    public StatisticListResponseDTO(String areaGu, List<StatisticLocationCountResponseDTO> list) {
        this.areaGu = areaGu;
        this.list = list;
    }
}
