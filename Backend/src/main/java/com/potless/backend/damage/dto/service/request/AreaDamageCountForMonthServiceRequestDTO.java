package com.potless.backend.damage.dto.service.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.YearMonth;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AreaDamageCountForMonthServiceRequestDTO {

    private YearMonth start;

    private YearMonth end;


    @Builder
    public AreaDamageCountForMonthServiceRequestDTO(YearMonth start, YearMonth end) {
        this.start = start;
        this.end = end;
    }

    public YearMonth getEndOrStart() {
        return (end != null) ? end : start;
    }
}
