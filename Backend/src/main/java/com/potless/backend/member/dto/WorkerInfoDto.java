package com.potless.backend.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "작업자 정보 DTO")
public class WorkerInfoDto {

    @Schema(description = "회원 ID, 가입되지 않은 경우 NULL로 표시")
    private Long memberId;
    @Schema(description = "작업자 이름")
    private String workerName;
}
