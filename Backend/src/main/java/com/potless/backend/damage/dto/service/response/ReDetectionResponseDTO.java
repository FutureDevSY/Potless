package com.potless.backend.damage.dto.service.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReDetectionResponseDTO {

    // 탐지된 포트홀의 위험도 등급, 1~3으로 분류
    private int severity;

}
