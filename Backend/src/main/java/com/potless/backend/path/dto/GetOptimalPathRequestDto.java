package com.potless.backend.path.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetOptimalPathRequestDto {

    private Location origin;
    private Long projectId;

}
