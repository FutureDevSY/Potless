package com.potless.backend.project.repository.project.custom;

import com.potless.backend.damage.dto.controller.response.DamageResponseDTO;
import com.potless.backend.damage.dto.controller.response.ImagesResponseDTO;
import com.potless.backend.damage.entity.enums.Status;
import com.potless.backend.damage.entity.road.QDamageEntity;
import com.potless.backend.damage.entity.road.QImageEntity;
import com.potless.backend.member.entity.QTeamEntity;
import com.potless.backend.project.dto.request.ProjectListRequestDto;
import com.potless.backend.project.dto.response.*;
import com.potless.backend.project.entity.ProjectEntity;
import com.potless.backend.project.entity.QProjectEntity;
import com.potless.backend.project.entity.QTaskEntity;
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

import static com.potless.backend.damage.entity.road.QDamageEntity.damageEntity;
import static com.potless.backend.damage.entity.road.QImageEntity.imageEntity;
import static com.potless.backend.member.entity.QTeamEntity.teamEntity;
import static com.potless.backend.project.entity.QProjectEntity.projectEntity;
import static com.potless.backend.project.entity.QTaskEntity.taskEntity;

public class ProjectRepositoryCustomImpl implements ProjectRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ProjectRepositoryCustomImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public ProjectDetailResponseDto getProjectDetail(Long projectId) {
        QProjectEntity project = projectEntity;
        QDamageEntity damage = damageEntity;
        QImageEntity image = imageEntity;
        QTaskEntity task = taskEntity;
        QTeamEntity team = teamEntity;

        ProjectEntity projectEntity = queryFactory
                .selectFrom(project)
                .leftJoin(project.teamEntity, team)
                .where(project.id.eq(projectId))
                .fetchOne();

        String teamName = (projectEntity.getTeamEntity() != null) ? projectEntity.getTeamEntity().getTeamName() : null;

        List<Long> damageIds = queryFactory
                .select(task.damageEntity.id)
                .from(task)
                .where(task.projectEntity.id.eq(projectId))
                .fetch();

        List<DamageDetailToProjectDto> damageResponses = queryFactory
                .select(Projections.constructor(DamageDetailToProjectDto.class,
                        damage.id,
                        task.id,
                        damage.severity,
                        damage.dirX,
                        damage.dirY,
                        damage.address,
                        damage.width,
                        damage.status,
                        damage.areaEntity.areaGu,
                        damage.locationEntity.locationName,
                        damage.dtype,
                        damage.createdDateTime
                ))
                .from(damage)
                .join(task).on(task.damageEntity.id.eq(damage.id))
                .where(task.projectEntity.id.eq(projectId))
                .fetch();

        for (DamageDetailToProjectDto damageDetailToProjectDto : damageResponses) {
            List<ImagesResponseDTO> imagesForDamage = queryFactory
                    .select(Projections.constructor(ImagesResponseDTO.class,
                            image.id,
                            image.url,
                            image.order))
                    .from(image)
                    .where(image.damageEntity.id.eq(damageDetailToProjectDto.getDamageId()))
                    .fetch();
            damageDetailToProjectDto.setImagesResponseDTOS(imagesForDamage);
        }

        List<TaskDetailDto> taskResponses = queryFactory
                .select(Projections.constructor(TaskDetailDto.class,
                        task.id,
                        task.damageEntity.id))
                .from(task)
                .where(task.projectEntity.id.eq(projectId))
                .fetch();

        return new ProjectDetailResponseDto(
                projectEntity.getProjectName(),
                projectEntity.getManagerEntity().getMemberEntity().getMemberName(),
                teamName,
                projectEntity.getProjectSize(),
                projectEntity.getProjectDate(),
                damageResponses
        );
    }

    @Override
    public Page<ProjectListResponseDto> findProjectAll(ProjectListRequestDto projectListRequestDto, Pageable pageable) {
        QProjectEntity project = projectEntity;
        QTeamEntity team = teamEntity;

        BooleanBuilder builder = new BooleanBuilder();

        builder.and(betweenDates(project, projectListRequestDto.getStart(), projectListRequestDto.getEnd()))
                .and(equalToStatus(project, projectListRequestDto.getStatus()))
                .and(containsSearchWord(project, projectListRequestDto.getWord()))
                .and(areaGuEquals(project, projectListRequestDto.getAreaId()))
                .and(project.deleted.eq(false));


        List<ProjectListResponseDto> results = queryFactory
                .select(Projections.constructor(ProjectListResponseDto.class,
                        project.id,
                        project.projectName,
                        project.managerEntity.memberEntity.memberName.as("managerName"),
                        project.projectDate,
                        project.projectSize,
                        project.createdDateTime,
                        team.teamName.as("teamName")
                ))
                .from(project)
                .leftJoin(project.teamEntity, team)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(project.createdDateTime.desc())
                .fetch();

        Long countResult = queryFactory
                .select(project.count())
                .from(project)
                .where(builder)
                .fetchOne();

        long total = (countResult != null) ? countResult : 0;
        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public List<GetTaskResponseDto> findProjectAndTaskByTeamId(List<Long> teamIdList) {
        List<GetTaskResponseDto> responseDtoList =
                queryFactory.select(Projections.constructor(GetTaskResponseDto.class,
                                projectEntity.teamEntity.id,
                                projectEntity.id,
                                projectEntity.projectName,
                                projectEntity.projectDate,
                                projectEntity.projectSize,
                                projectEntity.createdDateTime
                        ))
                        .from(projectEntity)
                        .join(teamEntity).on(teamEntity.id.eq(projectEntity.teamEntity.id))
                        .where(projectEntity.teamEntity.id.in(teamIdList)
                                .and(projectEntity.status.eq(Status.작업전))
                                .and(projectEntity.deleted.eq(false)))
                        .fetch();

        for (GetTaskResponseDto dto : responseDtoList) {
            List<DamageResponseDTO> damageList =
                    queryFactory.select(Projections.constructor(DamageResponseDTO.class, damageEntity))
                            .from(taskEntity)
                            .join(damageEntity).on(taskEntity.damageEntity.id.eq(damageEntity.id))
                            .where(taskEntity.projectEntity.id.eq(dto.getProjectId()))
                            .orderBy(taskEntity.taskOrder.asc())
                            .fetch();

            for (DamageResponseDTO damageResponseDTO : damageList) {
                List<ImagesResponseDTO> imagesForDamage = queryFactory
                        .select(Projections.constructor(ImagesResponseDTO.class,
                                imageEntity.id,
                                imageEntity.url,
                                imageEntity.order))
                        .from(imageEntity)
                        .where(imageEntity.damageEntity.id.eq(damageResponseDTO.getId()))
                        .fetch();
                damageResponseDTO.setImagesResponseDTOS(imagesForDamage);
            }
            dto.setDamangeResponseDtoList(damageList);
        }

        return responseDtoList;
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