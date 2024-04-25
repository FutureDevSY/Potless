package Potless.Backend.damage.dto.controller.request;

import Potless.Backend.damage.entity.enums.Status;
import lombok.*;

import java.time.LocalDate;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DamageSearchRequestDTO {

    private LocalDate start;
    private LocalDate end;
    private String type;
    private Status status;
    private Integer severity;
    private String area;
    private String searchWord;

    @Builder
    public DamageSearchRequestDTO(LocalDate start, LocalDate end, String type, Status status, Integer severity, String area, String searchWord) {
        if (start == null && end == null) {
            start = LocalDate.now().minusWeeks(1);
            end = LocalDate.now();
        } else if (start == null)
            start = end.minusWeeks(1);
        else if (end == null)
            end = LocalDate.now();

        this.start = start;
        this.end = end;
        this.type = type;
        this.status = status;
        this.severity = severity;
        this.area = area;
        this.searchWord = searchWord;
    }
}
