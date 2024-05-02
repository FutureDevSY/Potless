package com.potless.backend.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "지역별 팀 및 작업자 목록 조회 요청 DTO")
public class GetTeamResponseDto {

    @Schema(description = "팀 ID")
    private Long teamId;
    @Schema(description = "팀명")
    private String teamName;
    @Schema(description = "지역구 ID")
    private Long areaId;
    /* 구 (ex- 대덕구, 유성구, 중구 ..)*/
    @Schema(description = "지역구 (ex - 대덕구, 유성구, 중구 ..")
    private String areaGu;

    @Setter
    @Schema(description = "작업자 목록 DTO 리스트")
    private List<WorkerInfoDto> workerList;

    public GetTeamResponseDto(Long teamId, String teamName, Long areaId, String areaGu){
        this.teamId = teamId;
        this.teamName = teamName;
        this.areaId = areaId;
        this.areaGu = areaGu;
    }
}
