package com.potless.backend.project.dto.response;

import com.potless.backend.damage.dto.controller.response.DamageResponseDTO;
import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "작업 목록 조회 응답 DTO")
public class GetTaskResponseDto {

    @Schema(description = "팀 ID")
    private Long teamId;

    @Schema(description = "프로젝트 ID")
    private Long projectId;

    @Schema(description = "프로젝트명")
    private String projectName;

    @Schema(description = "프로젝트 날짜")
    private LocalDate projectDate;

    @Schema(description = "작업할 포트홀 개수")
    private Integer projectSize;

    @Schema(description = "프로젝트 생성 일자")
    private LocalDateTime createdDate;

    @Setter
    @Schema(description = "프로젝트별 할당된 작업 목록 DTO 리스트")
    private List<DamageResponseDTO> damangeResponseDtoList;

    public GetTaskResponseDto(Long teamId, Long projectId, String projectName, LocalDate projectDate, Integer projectSize, LocalDateTime createdDate) {
        this.teamId = teamId;
        this.projectId = projectId;
        this.projectName = projectName;
        this.projectDate = projectDate;
        this.projectSize = projectSize;
        this.createdDate = createdDate;
    }

}
