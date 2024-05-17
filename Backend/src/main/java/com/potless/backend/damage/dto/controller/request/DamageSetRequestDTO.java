package com.potless.backend.damage.dto.controller.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DamageSetRequestDTO {

    @NotEmpty(message = "타입 값이 비어있습니다")
    private String dtype;
    @NotNull(message = "x 값을 입력 해 주세요")
    private Double x;
    @NotNull(message = "y 값을 입력 해 주세요")
    private Double y;
    @Setter
    private int severity;
    @Setter
    private Double width;
    @Setter
    private Long memberId;
    @Setter
    private List<String> images = new ArrayList<>();

    @Builder
    public DamageSetRequestDTO(String dtype, Double x, Double y) {
        this.dtype = dtype;
        this.x = x;
        this.y = y;
    }
}
