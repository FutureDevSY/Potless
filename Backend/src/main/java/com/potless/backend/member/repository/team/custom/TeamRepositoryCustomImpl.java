package com.potless.backend.member.repository.team.custom;

import com.potless.backend.member.dto.GetTeamResponseDto;
import com.potless.backend.member.dto.WorkerInfoDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static com.potless.backend.damage.entity.area.QAreaEntity.areaEntity;
import static com.potless.backend.member.entity.QManagerEntity.managerEntity;
import static com.potless.backend.member.entity.QTeamEntity.teamEntity;
import static com.potless.backend.member.entity.QWorkerEntity.workerEntity;

@RequiredArgsConstructor
public class TeamRepositoryCustomImpl implements TeamRepositoryCustom {

    private final JPAQueryFactory query;


    @Override
    public Optional<Long> getAreaIdByAreaGu(String area){
        return Optional.ofNullable(
                        query.select(areaEntity.id)
                             .from(areaEntity)
                             .where(areaEntity.areaGu.like(area))
                             .fetchOne());
    }

    @Override
    public List<GetTeamResponseDto> getTeamListByArea(String area) {
        return query.select(Projections.constructor(GetTeamResponseDto.class,
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

    }

    @Override
    public List<WorkerInfoDto> getWorkerListByTeamId(Long teamId){
            return query.select(Projections.constructor(WorkerInfoDto.class,
                                workerEntity.memberEntity.Id,
                                workerEntity.workerName))
                        .from(workerEntity)
                        .where(workerEntity.teamEntity.id.eq(teamId))
                        .fetch();

    }

}
