package com.potless.backend.member.controller;

import com.potless.backend.global.format.code.ApiResponse;
import com.potless.backend.global.format.response.ResponseCode;
import com.potless.backend.member.dto.TeamAddRequestDto;
import com.potless.backend.member.service.TeamService;
import com.potless.backend.project.dto.request.CreateTeamRequestDto;
import com.potless.backend.project.dto.request.WorkerRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/team")
@Tag(name = "Team 컨트롤러", description = "Team Controller API")
public class TeamController {
    private final TeamService teamService;
    private final ApiResponse response;

    @Operation(summary = "Project에 Team 입력")
    @PostMapping("/project")
    public ResponseEntity<?> addTeamToProject(@RequestBody TeamAddRequestDto teamAddRequestDto) {
        Long result = teamService.addTeamToProject(teamAddRequestDto);
        return response.success(ResponseCode.TEAM_CONNECT_TO_PROJECT);
    }

    @PostMapping
    @Operation(summary = "팀 생성", description = "팀 생성 및 팀 작업자 추가 요청")
    public ResponseEntity<?> createTeam(@Parameter(hidden = true) Authentication authentication,
                                        @RequestBody CreateTeamRequestDto requestDto) {

        return response.success(ResponseCode.TEAM_CREATED, teamService.createTeam(authentication, requestDto));
    }

    @PostMapping("/worker")
    @Operation(summary = "팀 작업자 추가", description = "기존 팀에 신규 작업자 추가 요청")
    public ResponseEntity<?> addWorker(@Parameter(hidden = true) Authentication authentication,
                                       @RequestBody WorkerRequestDto requestDto) {

        return response.success(ResponseCode.TEAM_WORKER_ADDED, teamService.addWorker(authentication, requestDto));
    }

    @DeleteMapping("/worker")
    @Operation(summary = "팀 작업자 삭제", description = "기존 팀에 기존 작업자 삭제 요청")
    public ResponseEntity<?> deleteWorker(@Parameter(hidden = true) Authentication authentication,
                                          @RequestBody WorkerRequestDto requestDto) {

        return response.success(ResponseCode.TEAM_WORKER_DELETED, teamService.deleteWorker(authentication, requestDto));
    }

    @GetMapping
    @Operation(summary = "지역별 팀 조회", description = "지역별 팀 목록 조회 요청")
    public ResponseEntity<?> getTeam(@RequestParam(name = "area") String area) {

        return response.success(ResponseCode.TEAM_FETCHED, teamService.getTeam(area));
    }


}
