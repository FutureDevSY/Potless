package Potless.Backend.project.repository.project.custom;

import Potless.Backend.damage.entity.enums.Status;
import Potless.Backend.project.dto.request.ProjectListRequestDto;
import Potless.Backend.project.dto.response.ProjectListResponseDto;
import Potless.Backend.project.entity.QProjectEntity;
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
    public Page<ProjectListResponseDto> findProjectAllByManagerId(ProjectListRequestDto projectListRequestDto, Pageable pageable) {
        QProjectEntity project = QProjectEntity.projectEntity;
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(betweenDates(project, projectListRequestDto.getStart(), projectListRequestDto.getEnd()))
//                .and(equalToStatus(project, projectListRequestDto.getStatus()))
                .and(containsSearchWord(project, projectListRequestDto.getWord()));

        List<ProjectListResponseDto> results = queryFactory
                .select(Projections.constructor(ProjectListResponseDto.class,
                        project.id,
                        project.projectName,
                        project.managerEntity,
                        project.projectDate,
                        project.projectSize,
                        project.status
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

    //날짜
    private BooleanExpression betweenDates(QProjectEntity project, LocalDate start, LocalDate end) {
        if (start != null && end != null) {
            return project.createdDateTime.between(start.atStartOfDay(), end.atTime(23,59,59));
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

//    //지역
//    private BooleanExpression containsArea(QProjectEntity project, String area) {
//        if (area != null) {
//            return project.areaEntity.areaGu.contains(area);
//        }
//        return null;
//    }

    //검색어
    private BooleanExpression containsSearchWord(QProjectEntity project, String word) {
        if (word != null) {
            BooleanExpression managerNameMatch = project.managerEntity.memberEntity.memberName.contains(word);
            BooleanExpression projectTitleMatch = project.projectName.contains(word);
            return managerNameMatch.or(projectTitleMatch);
        }
        return null;
    }

}