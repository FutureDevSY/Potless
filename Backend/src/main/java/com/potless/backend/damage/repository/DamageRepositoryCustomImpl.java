package com.potless.backend.damage.repository;

import com.potless.backend.damage.dto.controller.request.DamageSearchRequestDTO;
import com.potless.backend.damage.dto.controller.request.DamageVerificationRequestDTO;
import com.potless.backend.damage.dto.controller.response.DamageResponseDTO;
import com.potless.backend.damage.dto.controller.response.ImagesResponseDTO;
import com.potless.backend.damage.dto.service.response.StatisticCountResponseDTO;
import com.potless.backend.damage.dto.service.response.StatisticListResponseDTO;
import com.potless.backend.damage.dto.service.response.StatisticLocationCountResponseDTO;
import com.potless.backend.damage.entity.area.AreaEntity;
import com.potless.backend.damage.entity.area.QLocationEntity;
import com.potless.backend.damage.entity.area.QAreaEntity;
import com.potless.backend.damage.entity.enums.Status;
import com.potless.backend.damage.entity.road.QCrackEntity;
import com.potless.backend.damage.entity.road.QDamageEntity;
import com.potless.backend.damage.entity.road.QImageEntity;
import com.potless.backend.damage.entity.road.QPotholeEntity;
import com.potless.backend.member.entity.QMemberEntity;
import com.potless.backend.member.entity.QTeamEntity;
import com.potless.backend.member.entity.QWorkerEntity;
import com.potless.backend.project.entity.QProjectEntity;
import com.potless.backend.project.entity.QTaskEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class DamageRepositoryCustomImpl implements DamageRepositoryCustom {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public DamageRepositoryCustomImpl(EntityManager entityManager) {
        this.em = entityManager;
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

//    @Override
//    public List<DamageResponseDTO> findDamagesByWorker(Long memberId) {
//        QDamageEntity damage = QDamageEntity.damageEntity;
//        QImageEntity image = QImageEntity.imageEntity;
//        QTeamEntity team = QTeamEntity.teamEntity;
//        QWorkerEntity worker = QWorkerEntity.workerEntity;
//        QTaskEntity task = QTaskEntity.taskEntity;
//        QMemberEntity member = QMemberEntity.memberEntity;
//        QProjectEntity project = QProjectEntity.projectEntity;
//        QAreaEntity area = QAreaEntity.areaEntity;
//
//        List<DamageResponseDTO> results = queryFactory
//                .select(Projections.constructor(DamageResponseDTO.class,
//                        damage.id,
//                        damage.severity,
//                        damage.dirX,
//                        damage.dirY,
//                        damage.address,
//                        damage.roadName,
//                        damage.width,
//                        damage.status,
//                        damage.areaEntity.areaGu,
//                        damage.locationEntity.locationName,
//                        damage.dtype
//                ))
//                .from(damage)
//                .innerJoin(damage.en)
//                .innerJoin(area.en)
//
//    }


    @Override
    public Page<DamageResponseDTO> findDamagesWithLatestTransaction(DamageSearchRequestDTO searchDTO, Pageable pageable) {
        QDamageEntity damage = QDamageEntity.damageEntity;
        QImageEntity image = QImageEntity.imageEntity;

        BooleanBuilder builder = new BooleanBuilder();

        builder.and(betweenDates(damage, searchDTO.getStart(), searchDTO.getEnd()))
                .and(equalToStatus(damage, searchDTO.getStatus()))
                .and(equalToSeverity(damage, searchDTO.getSeverity()))
                .and(containsArea(damage, searchDTO.getArea()))
                .and(containsSearchWord(damage, searchDTO.getSearchWord()));

        // 기타 코드 처리

        List<DamageResponseDTO> results;

        if ("CRACK".equals(searchDTO.getType())) {
            QCrackEntity crack = QCrackEntity.crackEntity;

            results = queryFactory
                    .select(Projections.constructor(DamageResponseDTO.class,
                            damage.id,
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
                    .from(crack)
                    .where(builder)
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();

            Long countResult = queryFactory
                    .select(crack.count())
                    .from(crack)
                    .where(builder)
                    .fetchOne();

            long total = (countResult != null) ? countResult : 0;

            return new PageImpl<>(results, pageable, total);

        } else if ("POTHOLE".equals(searchDTO.getType())) {
            QPotholeEntity pothole = QPotholeEntity.potholeEntity;

            results = queryFactory
                    .select(Projections.constructor(DamageResponseDTO.class,
                            damage.id,
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
                    .from(pothole)
                    .where(builder)
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();

            Long countResult = queryFactory
                    .select(pothole.count())
                    .from(pothole)
                    .where(builder)
                    .fetchOne();

            long total = (countResult != null) ? countResult : 0;

            return new PageImpl<>(results, pageable, total);
        } else {
            // 베이스 엔티티 또는 기타 타입을 기본적으로 쿼리
            results = queryFactory
                    .select(Projections.constructor(DamageResponseDTO.class,
                            damage.id,
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
                    .where(builder)
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();

            for (DamageResponseDTO damageResponseDTO : results) {
                List<ImagesResponseDTO> imagesForDamage = queryFactory
                        .select(Projections.constructor(ImagesResponseDTO.class,
                                image.id,
                                image.url,
                                image.order))
                        .from(image)
                        .where(image.damageEntity.id.eq(damageResponseDTO.getId()))
                        .fetch();
                damageResponseDTO.setImagesResponseDTOS(imagesForDamage);
            }

            Long countResult = queryFactory
                    .select(damage.count())
                    .from(damage)
                    .where(builder)
                    .fetchOne();

            long total = (countResult != null) ? countResult : 0;

            return new PageImpl<>(results, pageable, total);
        }
    }

    @Override
    public List<DamageResponseDTO> findDamagesByVerificationRequest(DamageVerificationRequestDTO verificationRequest) {

        QDamageEntity damage = QDamageEntity.damageEntity;
        QImageEntity image = QImageEntity.imageEntity;

        BooleanBuilder builder = new BooleanBuilder();
        if (verificationRequest.getDtype() != null) {
            builder.and(damage.dtype.eq(verificationRequest.getDtype()));
        }
        if (verificationRequest.getDamageAddress() != null) {
            builder.and(damage.address.contains(verificationRequest.getDamageAddress()));
        }
        if (verificationRequest.getArea() != null) {
            builder.and(damage.areaEntity.areaGu.contains(verificationRequest.getArea()));
        }
        if (verificationRequest.getLocation() != null) {
            builder.and(damage.locationEntity.locationName.contains(verificationRequest.getLocation()));
        }

        List<DamageResponseDTO> results = queryFactory
                .select(Projections.constructor(DamageResponseDTO.class,
                        damage.id,
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
                .where(builder)
                .fetch();

        for (DamageResponseDTO damageResponseDTO : results) {
            List<ImagesResponseDTO> imagesForDamage = queryFactory
                    .select(Projections.constructor(ImagesResponseDTO.class,
                            image.id,
                            image.url,
                            image.order))
                    .from(image)
                    .where(image.damageEntity.id.eq(damageResponseDTO.getId()))
                    .fetch();
            damageResponseDTO.setImagesResponseDTOS(imagesForDamage);
        }
        return results;
    }

    @Override
    public StatisticListResponseDTO getStatistic(Long areaId) {
        QDamageEntity damage = QDamageEntity.damageEntity;
        QLocationEntity location = QLocationEntity.locationEntity;
        QAreaEntity area = QAreaEntity.areaEntity;

        List<Tuple> rawResults = queryFactory
                .select(location.id, location.locationName, damage.status, damage.count())
                .from(location)
                .leftJoin(location.damageEntities, damage)
                .where(location.areaEntity.id.eq(areaId))
                .groupBy(location.id, damage.status)
                .fetch();

        List<StatisticLocationCountResponseDTO> results = rawResults.stream()
                .collect(Collectors.groupingBy(
                        tuple -> tuple.get(location.id),
                        Collectors.mapping(tuple -> new StatisticLocationCountResponseDTO(
                                        tuple.get(location.locationName),
                                        tuple.get(damage.status) == Status.작업전 ? tuple.get(damage.count()) : 0L,
                                        tuple.get(damage.status) == Status.작업완료 ? tuple.get(damage.count()) : 0L
                                ),
                                Collectors.toList()
                        )
                ))
                .values()
                .stream()
                .flatMap(list -> list.stream().reduce((dto1, dto2) -> new StatisticLocationCountResponseDTO(
                        dto1.getLocationName(),
                        dto1.getCountDamageBefore() + dto2.getCountDamageBefore(),
                        dto1.getCountDamageDone() + dto2.getCountDamageDone()
                )).stream())
                .collect(Collectors.toList());

        String areaGu = em.find(AreaEntity.class, areaId).getAreaGu();
        return new StatisticListResponseDTO(areaGu, results);
    }


    @Override
    public List<StatisticCountResponseDTO> getStatistics() {
        QDamageEntity damage = QDamageEntity.damageEntity;
        QAreaEntity area = QAreaEntity.areaEntity;

        List<Tuple> results = queryFactory
                .select(area.areaGu,
                        damage.status,
                        damage.count().as("count"))
                .from(damage)
                .join(damage.areaEntity, area)
                .groupBy(area.areaGu, damage.status)
                .fetch();

        Map<String, Map<Status, Long>> groupedResults = results.stream().collect(Collectors.groupingBy(
                result -> Optional.ofNullable(result.get(area.areaGu)).orElse("Unknown Area"),
                Collectors.toMap(
                        result -> result.get(damage.status),
                        result -> Optional.ofNullable(result.get(Expressions.numberPath(Long.class, "count"))).orElse(0L),
                        (u, v) -> {
                            throw new IllegalStateException(String.format("Duplicate key %s", u));
                        }
                )
        ));
        return groupedResults.entrySet().stream()
                .map(entry -> new StatisticCountResponseDTO(
                        entry.getKey(),
                        entry.getValue().getOrDefault(Status.작업전, 0L),
                        entry.getValue().getOrDefault(Status.작업완료, 0L)
                ))
                .collect(Collectors.toList());
    }

    @Override
    public StatisticLocationCountResponseDTO getStatisticLocation(String locationName) {
        QDamageEntity damage = QDamageEntity.damageEntity;
        // 상태별로 결과를 그룹화하고 카운트
        List<Tuple> counts = queryFactory
                .select(damage.status, damage.count())
                .from(damage)
                .where(damage.locationEntity.locationName.eq(locationName))
                .groupBy(damage.status)
                .fetch();

        Long countDamageBefore = 0L;
        Long countDamageDone = 0L;

        // Tuple 결과 처리
        for (Tuple tuple : counts) {
            Status status = tuple.get(damage.status);
            Long count = tuple.get(1, Long.class); // 카운트 값 가져오기

            if (status == Status.작업전) {
                countDamageBefore = count;
            } else if (status == Status.작업완료) {
                countDamageDone = count;
            }
        }

        return StatisticLocationCountResponseDTO.builder()
                .locationName(locationName)
                .countDamageBefore(countDamageBefore)
                .countDamageDone(countDamageDone)
                .build();
    }


    @Override
    public List<StatisticLocationCountResponseDTO> getStatisticLocations() {
        QDamageEntity damage = QDamageEntity.damageEntity;
        QLocationEntity location = QLocationEntity.locationEntity;

        List<Tuple> results = queryFactory
                .select(location.locationName,
                        damage.status,
                        damage.count().as("count"))
                .from(damage)
                .join(damage.locationEntity, location)
                .groupBy(location.locationName, damage.status)
                .fetch();

        Map<String, Map<Status, Long>> groupedResults = results.stream().collect(Collectors.groupingBy(
                result -> Optional.ofNullable(result.get(location.locationName)).orElse("Unknown Location"),
                Collectors.toMap(
                        result -> result.get(damage.status),
                        result -> Optional.ofNullable(result.get(Expressions.numberPath(Long.class, "count"))).orElse(0L),
                        (u, v) -> {
                            throw new IllegalStateException(String.format("Duplicate key %s", u));
                        }
                )
        ));

        return groupedResults.entrySet().stream()
                .map(entry -> new StatisticLocationCountResponseDTO(
                        entry.getKey(),
                        entry.getValue().getOrDefault(Status.작업전, 0L),
                        entry.getValue().getOrDefault(Status.작업완료, 0L)
                ))
                .collect(Collectors.toList());
    }

    private BooleanExpression betweenDates(QDamageEntity damage, LocalDate start, LocalDate end) {
        if (start != null && end != null) {
            return damage.createdDateTime.between(start.atStartOfDay(), end.atTime(23, 59, 59));
        }
        return null;
    }

    private BooleanExpression equalToStatus(QDamageEntity damage, Status status) {
        if (status != null) {
            return damage.status.eq(status);
        }
        return null;
    }

    private BooleanExpression equalToSeverity(QDamageEntity damage, Integer severity) {
        if (severity != null) {
            return damage.severity.eq(severity);
        }
        return null;
    }

    private BooleanExpression containsArea(QDamageEntity damage, String area) {
        if (area != null) {
            return damage.areaEntity.areaGu.contains(area);
        }
        return null;
    }

    private BooleanExpression containsSearchWord(QDamageEntity damage, String searchWord) {
        if (searchWord != null) {
            return damage.locationEntity.locationName.contains(searchWord);
        }
        return null;
    }
}
