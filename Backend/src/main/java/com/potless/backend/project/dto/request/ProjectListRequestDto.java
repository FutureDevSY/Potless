package com.potless.backend.project.dto.request;

import com.potless.backend.damage.entity.enums.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class ProjectListRequestDto {
    private Long managerId;
    private LocalDate start;
    private LocalDate end;
    private Status status;
    private String word;

    public ProjectListRequestDto(Long managerId, LocalDate start, LocalDate end, Status status, String word) {
        if (start == null && end == null) {
            start = LocalDate.now().minusWeeks(1);
            end = LocalDate.now();
        } else if (start == null)
            start = end.minusWeeks(1);
        else if (end == null)
            end = LocalDate.now();

        this.managerId = managerId;
        this.start = start;
        this.end = end;
        this.status = status;
        this.word = word;
    }
}
