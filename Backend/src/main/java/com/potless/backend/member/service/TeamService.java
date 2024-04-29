package com.potless.backend.member.service;

import com.potless.backend.member.dto.TeamAddRequestDto;

public interface TeamService {
    Long addTeamToProject(TeamAddRequestDto teamAddRequestDto);
}
