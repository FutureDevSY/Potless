package com.potless.backend.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class WorkerInfoDto {

    private Long memberId;
    private String workerName;
}
