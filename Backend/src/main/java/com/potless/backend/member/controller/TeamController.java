package com.potless.backend.member.controller;

import com.potless.backend.global.format.code.ApiResponse;
import com.potless.backend.global.format.response.ResponseCode;
import com.potless.backend.member.dto.TeamAddRequestDto;
import com.potless.backend.member.service.TeamService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/team")
@Tag(name = "Team 컨트롤러", description = "Team Controller API")
public class TeamController {
    private final TeamService teamService;
    private final ApiResponse response;

    @PostMapping
    public ResponseEntity<?> addTeamToProject(@RequestBody TeamAddRequestDto teamAddRequestDto) {
        Long result = teamService.addTeamToProject(teamAddRequestDto);
        return response.success(ResponseCode.TEAM_CONNECT_TO_PROJECT);
    }
}
