package com.potless.backend.member.dto;

import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetTeamResponseDto {

    private Long teamId;
    private String teamName;
    private Long areaId;
    /* 구 (ex- 대덕구, 유성구, 중구 ..)*/
    private String areaGu;

    @Setter
    private List<WorkerInfoDto> workerList;

    public GetTeamResponseDto(Long teamId, String teamName, Long areaId, String areaGu){
        this.teamId = teamId;
        this.teamName = teamName;
        this.areaId = areaId;
        this.areaGu = areaGu;
    }
}
