package com.potless.backend.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@Schema(description = "지역별 작업자 목록 조회 요청 DTO (팀에 할당되지 않은 작업자 포함)")
public class GetWorkerResponseDto {

    @Schema(description = "회원 ID, 가입되지 않은 경우 NULL로 표시")
    private Long memberId;
    @Schema(description = "작업자 이름")
    private String workerName;
    @Schema(description = "팀 ID")
    private Long teamId;
}
