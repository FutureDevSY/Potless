package com.potless.backend.member.repository.team.custom;

import com.potless.backend.member.dto.GetTeamResponseDto;
import com.potless.backend.member.dto.WorkerInfoDto;
import com.potless.backend.member.entity.TeamEntity;

import java.util.List;
import java.util.Optional;

public interface TeamRepositoryCustom {

    public List<GetTeamResponseDto> getTeamListByArea(Long areaId);

    public Optional<Long> getAreaIdByAreaGu(String area);

    public List<WorkerInfoDto> getWorkerListByTeamId(Long teamId);

    public List<TeamEntity> findByMemberId(Long memberId);
}
