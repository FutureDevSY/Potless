package com.potless.backend.member.repository.worker.custom;

import com.potless.backend.member.entity.WorkerEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.potless.backend.member.entity.QWorkerEntity.workerEntity;

@RequiredArgsConstructor
@Log4j2
public class WorkerRepositoryCustomImpl implements WorkerRepositoryCustom {

    private final JPAQueryFactory query;

    @Override
    public Long deleteByNameAtOnce(List<String> nameList) {
        Map<String, Integer> nameCounts = new HashMap<>();
        Long deletedCount = 0L;
        for (String name : nameList) {
            nameCounts.put(name, nameCounts.getOrDefault(name, 0) + 1);
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

}
