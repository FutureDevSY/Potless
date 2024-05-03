package com.potless.backend.member.repository.worker.custom;

import com.potless.backend.member.dto.WorkerInfoDto;
import com.potless.backend.member.entity.TeamEntity;
import com.potless.backend.member.entity.WorkerEntity;
import com.querydsl.core.types.Path;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.*;

import static com.potless.backend.member.entity.QWorkerEntity.workerEntity;

@RequiredArgsConstructor
@Log4j2
public class WorkerRepositoryCustomImpl implements WorkerRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public Long deleteByNameAtOnce(List<WorkerInfoDto> workerList) {

        Map<String, Integer> nameCounts = new HashMap<>();
        Long deletedCount = 0L;
        for (WorkerInfoDto workerInfo : workerList) {
            nameCounts.put(workerInfo.getWorkerName(), nameCounts.getOrDefault(workerInfo.getWorkerName(), 0) + 1);
        }

        for (Map.Entry<String, Integer> entry : nameCounts.entrySet()) {
            String name = entry.getKey();
            Integer count = entry.getValue();

            List<Long> ids = query.select(workerEntity.id)
                                         .from(workerEntity)
                                         .where(workerEntity.workerName.eq(name))
                                         .limit(count)
                                         .fetch();

            if (!ids.isEmpty()) {
                deletedCount = query.delete(workerEntity)
                                                .where(workerEntity.id.in(ids))
                                                .execute();
                log.info("Deleted " + deletedCount + " records of name: " + name);
            }
        }
        return deletedCount;
    }

    @Override
    public List<WorkerEntity> findAllByAreaId(Long areaId) {
        return query.selectFrom(workerEntity)
                    .where(workerEntity.areaEntity.id.eq(areaId))
                    .fetch();
    }

    @Override
    public void resetTeamFromWorkers(Long teamId) {
        query.update(workerEntity).set(workerEntity.teamEntity, (TeamEntity) null)
             .where(workerEntity.teamEntity.id.eq(teamId))
             .execute();
    }

    @Override
    public List<WorkerEntity> findAllByteamId(Long teamId) {
        return query.selectFrom(workerEntity)
                .where(workerEntity.teamEntity.id.eq(teamId))
                .fetch();
    }

    @Override
    public Optional<WorkerEntity> findByMemberId(Long memberId) {
        return Optional.ofNullable(query.selectFrom(workerEntity)
                           .where(workerEntity.memberEntity.Id.eq(memberId))
                           .fetchOne());
    }



}
