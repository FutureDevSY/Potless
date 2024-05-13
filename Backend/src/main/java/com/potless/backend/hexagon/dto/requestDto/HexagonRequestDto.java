package com.potless.backend.hexagon.dto.requestDto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@NoArgsConstructor
@ToString
public class HexagonRequestDto {
    @NotBlank(message = "latitude : 위도 필수값")
    private Double latitude;
    @NotBlank(message = "longitude : 경도 필수값")
    private Double longitude;
    @NotBlank(message = "타입 필수값")
    private String type;
}
