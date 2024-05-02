package com.potless.backend.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class GetWorkerResponseDto {

    private Long memberId;
    private String workerName;
    private Long teamId;
}
