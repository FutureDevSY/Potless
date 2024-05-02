package com.potless.backend.member.service;

import com.potless.backend.member.dto.GetTeamResponseDto;
import com.potless.backend.member.dto.GetWorkerResponseDto;
import com.potless.backend.member.dto.TeamAddRequestDto;
import com.potless.backend.member.dto.WorkerInfoDto;
import com.potless.backend.project.dto.request.CreateTeamRequestDto;
import com.potless.backend.project.dto.request.WorkerRequestDto;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface TeamService {
    Long addTeamToProject(TeamAddRequestDto teamAddRequestDto);

    Long createTeam(Authentication authentication, CreateTeamRequestDto createTeamRequestDto);

    Long addWorker(Authentication authentication, WorkerRequestDto requestDto);

    Long deleteWorker(Authentication authentication, WorkerRequestDto requestDto);

    List<GetTeamResponseDto> getTeam(String area);

    List<GetWorkerResponseDto> getWorker(String area);
}
