package com.potless.backend.member.repository.team.custom;

import com.potless.backend.damage.dto.controller.response.DamageResponseDTO;
import com.potless.backend.global.exception.project.AreaNotFoundException;
import com.potless.backend.global.exception.project.TeamNotFoundException;
import com.potless.backend.member.dto.GetTeamResponseDto;
import com.potless.backend.member.dto.WorkerInfoDto;
import com.potless.backend.member.entity.QTeamEntity;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.potless.backend.damage.entity.area.QAreaEntity.areaEntity;
import static com.potless.backend.member.entity.QManagerEntity.managerEntity;
import static com.potless.backend.member.entity.QTeamEntity.teamEntity;
import static com.potless.backend.member.entity.QWorkerEntity.workerEntity;

@RequiredArgsConstructor
public class TeamRepositoryCustomImpl implements TeamRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public List<GetTeamResponseDto> getTeamListByArea(String area) {

        Long areaId = query.select(areaEntity.id)
                           .from(areaEntity)
                           .where(areaEntity.areaGu.like(area))
                           .fetchOne();

        // 해당되는 area가 없는 경우
        if(areaId == null) throw new AreaNotFoundException();

        List<GetTeamResponseDto> teamList =
                query.select(Projections.constructor(GetTeamResponseDto.class,
                                         teamEntity.id,
                                         teamEntity.teamName,
                                         managerEntity.areaEntity.id,
                                         managerEntity.areaEntity.areaGu))
                     .from(managerEntity)
                     .where(managerEntity.areaEntity.id.eq(query.select(areaEntity.id)
                                                                .from(areaEntity)
                                                                .where(areaEntity.areaGu.like(area))
                                                                .fetchOne()))
                     .leftJoin(teamEntity)
                     .on(managerEntity.id.eq(teamEntity.managerEntity.id))
                     .fetch();

        // 해당되는 팀이 조회되지 않는 경우
        if(teamList == null || teamList.isEmpty()) throw new TeamNotFoundException();

        for (GetTeamResponseDto dto : teamList) {
            List<WorkerInfoDto> workerList = query.select(Projections.constructor(WorkerInfoDto.class, workerEntity.memberEntity.Id, workerEntity.workerName))
                                                  .from(workerEntity)
                                                  .where(workerEntity.teamEntity.id.eq(dto.getTeamId()))
                                                  .fetch();

            dto.setWorkerList(workerList);
        }

        return teamList;
    }
}
