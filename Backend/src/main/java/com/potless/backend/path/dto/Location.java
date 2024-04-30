package com.potless.backend.path.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Location {
    @Schema(description = "X 좌표(경도)")
    private Double x;
    @Schema(description = "Y 좌표(위도)")
    private Double y;
}
