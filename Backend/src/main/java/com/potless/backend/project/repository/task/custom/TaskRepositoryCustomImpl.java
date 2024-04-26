package com.potless.backend.project.repository.task.custom;

import com.potless.backend.project.entity.QTaskEntity;
import com.potless.backend.project.entity.TaskEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import java.util.List;

public class TaskRepositoryCustomImpl implements TaskRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public TaskRepositoryCustomImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<TaskEntity> findTasksByProjectId(Long projectId) {
        QTaskEntity qTaskEntity = QTaskEntity.taskEntity;

        return queryFactory
                .selectFrom(qTaskEntity)
                .where(qTaskEntity.projectEntity.id.eq(projectId))
                .fetch();
    }
}
