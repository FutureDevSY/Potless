package Potless.Backend.project.repository.task.custom;

import Potless.Backend.project.entity.QTaskEntity;
import Potless.Backend.project.entity.TaskEntity;
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
