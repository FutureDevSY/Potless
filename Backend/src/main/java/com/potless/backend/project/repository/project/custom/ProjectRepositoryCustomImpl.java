package com.potless.backend.project.repository.project.custom;

import com.potless.backend.damage.entity.enums.Status;
import com.potless.backend.project.dto.request.ProjectListRequestDto;
import com.potless.backend.project.dto.response.ProjectListResponseDto;
import com.potless.backend.project.entity.QProjectEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public class ProjectRepositoryCustomImpl implements ProjectRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ProjectRepositoryCustomImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<ProjectListResponseDto> findProjectAll(ProjectListRequestDto projectListRequestDto, Pageable pageable) {
        QProjectEntity project = QProjectEntity.projectEntity;
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(managerIdEquals(project, projectListRequestDto.getManagerId()))
                .and(betweenDates(project, projectListRequestDto.getStart(), projectListRequestDto.getEnd()))
                .and(equalToStatus(project,projectListRequestDto.getStatus()))
                .and(containsSearchWord(project, projectListRequestDto.getWord()))
                .and(areaGuEquals(project, projectListRequestDto.getAreaId()))
                .and(project.deleted.eq(false));


        List<ProjectListResponseDto> results = queryFactory
                .select(Projections.constructor(ProjectListResponseDto.class,
                        project.projectName,
                        project.managerEntity.memberEntity.memberName.as("managerName"),
                        project.projectDate,
                        project.projectSize
                ))
                .from(project)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long countResult = queryFactory
                .select(project.count())
                .from(project)
                .where(builder)
                .fetchOne();

        long total = (countResult != null) ? countResult : 0;
        return new PageImpl<>(results, pageable, total);
    }

    //매니저
    private BooleanExpression managerIdEquals(QProjectEntity project, Long managerId) {
        if (managerId != null) {
            return project.managerEntity.id.eq(managerId);
        }
        return null;
    }

    //날짜
    private BooleanExpression betweenDates(QProjectEntity project, LocalDate start, LocalDate end) {
        if (start != null && end != null) {
            return project.createdDateTime.between(start.atStartOfDay(), end.atTime(23, 59, 59));
        }
        return null;
    }

    //상태
    private BooleanExpression equalToStatus(QProjectEntity project, Status status) {
        if (status != null) {
            return project.status.eq(status);
        }
        return null;
    }

    //검색어
    private BooleanExpression containsSearchWord(QProjectEntity project, String word) {
        if (word != null) {
            BooleanExpression managerNameMatch = project.managerEntity.memberEntity.memberName.contains(word);
            BooleanExpression projectTitleMatch = project.projectName.contains(word);
            return managerNameMatch.or(projectTitleMatch);
        }
        return null;
    }
    private BooleanExpression areaGuEquals(QProjectEntity project, Long areaId) {
        if (areaId != null) {
            return project.areaEntity.id.eq(areaId);
        }
        return null;
    }


}