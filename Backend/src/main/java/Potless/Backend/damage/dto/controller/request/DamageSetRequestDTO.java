package Potless.Backend.damage.dto.controller.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DamageSetRequestDTO {

    @NotNull(message = "x 값을 입력 해 주세요")
    public Double x;

    @NotNull(message = "y 값을 입력 해 주세요")
    public Double y;

    @NotEmpty(message = "이미지값이 비어있습니다")
    public List<String> images = new ArrayList<>();

    @Builder
    public DamageSetRequestDTO(Double x, Double y, List<String> images) {
        this.x = x;
        this.y = y;
        this.images = images;
    }
}
