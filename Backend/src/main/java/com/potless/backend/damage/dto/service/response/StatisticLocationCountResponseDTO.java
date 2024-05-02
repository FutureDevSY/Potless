package com.potless.backend.damage.dto.service.response;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StatisticLocationCountResponseDTO {

    private String locationName;
    private Long countDamageBefore;
    private Long countDamageDone;

    @Builder
    public StatisticLocationCountResponseDTO(String locationName, Long countDamageBefore, Long countDamageDone) {
        this.locationName = locationName;
        this.countDamageBefore = countDamageBefore;
        this.countDamageDone = countDamageDone;
    }
}
