package com.potless.backend.project.dto.request;

import com.potless.backend.damage.entity.enums.Status;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Builder
@ToString
public class ProjectListRequestDto {
    private Long memberId;
    private Long areaId;
    private LocalDate start;
    private LocalDate end;
    private Status status;
    private String word;


    public ProjectListRequestDto(Long memberId, Long areaId, LocalDate start, LocalDate end, Status status, String word) {
        if (start == null && end == null) {
            start = LocalDate.now().minusWeeks(1);
            end = LocalDate.now();
        } else if (start == null)
            start = end.minusWeeks(1);
        else if (end == null)
            end = LocalDate.now();

        this.memberId = memberId;
        this.areaId = areaId;
        this.start = start;
        this.end = end;
        this.status = status;
        this.word = word;

    }
}
