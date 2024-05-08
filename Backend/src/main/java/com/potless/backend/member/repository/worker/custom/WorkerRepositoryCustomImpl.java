package com.potless.backend.member.repository.worker.custom;

import com.potless.backend.member.dto.WorkerInfoDto;
import com.potless.backend.member.entity.TeamEntity;
import com.potless.backend.member.entity.WorkerEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.potless.backend.member.entity.QWorkerEntity.workerEntity;

@RequiredArgsConstructor
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
    public List<WorkerEntity> findAllByMemberId(Long memberId) {
        return query.selectFrom(workerEntity)
                .where(workerEntity.memberEntity.Id.eq(memberId))
                .fetch();
    }

    @Override
    public Optional<WorkerEntity> findByMemberIdAndAreaWhereTeamIsEmpty(Long memberId, Long areaId) {
        return Optional.ofNullable(query.select(workerEntity)
                .from(workerEntity)
                .where(workerEntity.memberEntity.Id.eq(memberId)
                        .and(workerEntity.areaEntity.id.eq(areaId))
                        .and(workerEntity.teamEntity.isNull()))
                .fetchFirst());
    }

    @Override
    public List<Long> findMemberIdsWithDuplicatesWhereTeamIsNull() {
        return query.select(workerEntity.memberEntity.Id)
                .from(workerEntity)
                .where(workerEntity.teamEntity.id.isNull())
                .groupBy(workerEntity.memberEntity.Id)
                .having(workerEntity.memberEntity.Id.count().gt(1))
                .fetch();
    }

    @Override
    public boolean checkByMemberIdWhereTeamIsExist(Long memberId) {
        return query.selectOne()
                .from(workerEntity)
                .where(workerEntity.memberEntity.Id.eq(memberId).and(workerEntity.teamEntity.isNotNull()))
                .fetchFirst() != null;
    }


}
