package com.potless.backend.member.repository.team.custom;

import com.potless.backend.member.dto.GetTeamResponseDto;

import java.util.List;

public interface TeamRepositoryCustom {

    public List<GetTeamResponseDto> getTeamListByArea(String area);

}
