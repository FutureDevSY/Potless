package com.potless.backend.damage.repository;

import com.potless.backend.damage.dto.controller.request.AreaDamageCountForDateRequestDTO;
import com.potless.backend.damage.dto.controller.request.DamageSearchRequestDTO;
import com.potless.backend.damage.dto.controller.response.DamageResponseDTO;
import com.potless.backend.damage.dto.controller.response.ImagesResponseDTO;
import com.potless.backend.damage.dto.service.request.AreaDamageCountForMonthServiceRequestDTO;
import com.potless.backend.damage.dto.service.response.*;
import com.potless.backend.damage.dto.service.response.count.AreaDamageCountForDateListResponseDTO;
import com.potless.backend.damage.dto.service.response.count.AreaDamageCountForDateResponseDTO;
import com.potless.backend.damage.dto.service.response.count.AreaDamageCountForMonthListResponseDTO;
import com.potless.backend.damage.dto.service.response.count.AreaDamageCountForMonthResponseDTO;
import com.potless.backend.damage.entity.area.AreaEntity;
import com.potless.backend.damage.entity.area.QAreaEntity;
import com.potless.backend.damage.entity.area.QLocationEntity;
import com.potless.backend.damage.entity.enums.Status;
import com.potless.backend.damage.entity.road.QDamageEntity;
import com.potless.backend.damage.entity.road.QImageEntity;
import com.potless.backend.project.entity.QProjectEntity;
import com.potless.backend.project.entity.QTaskEntity;
import com.potless.backend.project.entity.TaskEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class DamageRepositoryCustomImpl implements DamageRepositoryCustom {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public DamageRepositoryCustomImpl(EntityManager entityManager) {
        this.em = entityManager;
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public AreaForDateListResponseDTO getAreaDamageCountForDate(AreaDamageCountForDateRequestDTO requestDTO) {
        LocalDate startDate = requestDTO.getStart();
        LocalDate endDate = requestDTO.getEnd() != null ? requestDTO.getEnd() : startDate;

        QDamageEntity damage = QDamageEntity.damageEntity;
        QAreaEntity area = QAreaEntity.areaEntity;

        // 모든 '구' 목록을 가져오기
        List<String> allAreas = queryFactory.select(area.areaGu)
                .from(area)
                .fetch();

        Map<String, Map<LocalDate, Long>> areaMap = new LinkedHashMap<>();

        // 모든 '구'에 대해 날짜별로 초기화
        for (String areaGu : allAreas) {
            TreeMap<LocalDate, Long> dateCounts = new TreeMap<>();
            for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                dateCounts.put(date, 0L);
            }
            areaMap.put(areaGu, dateCounts);
        }

        // 실제 데이터로 업데이트
        List<Tuple> rawResults = queryFactory
                .select(area.areaGu,
                        damage.createdDateTime.year(),
                        damage.createdDateTime.month(),
                        damage.createdDateTime.dayOfMonth(),
                        damage.count())
                .from(damage)
                .join(damage.areaEntity, area)
                .where(damage.createdDateTime.between(
                        startDate.atStartOfDay(), endDate.plusDays(1).atStartOfDay().minusSeconds(1)
                ))
                .groupBy(area.areaGu, damage.createdDateTime.year(), damage.createdDateTime.month(), damage.createdDateTime.dayOfMonth())
                .fetch();

        // 쿼리 결과 처리
        rawResults.forEach(tuple -> {
            String areaGu = tuple.get(area.areaGu);
            Optional<Integer> year = Optional.ofNullable(tuple.get(damage.createdDateTime.year()));
            Optional<Integer> month = Optional.ofNullable(tuple.get(damage.createdDateTime.month()));
            Optional<Integer> day = Optional.ofNullable(tuple.get(damage.createdDateTime.dayOfMonth()));

            // 모든 날짜 컴포넌트가 있는 경우에만 날짜를 생성하고 맵 업데이트
            if (year.isPresent() && month.isPresent() && day.isPresent()) {
                LocalDate date = LocalDate.of(year.get(), month.get(), day.get());
                Long count = tuple.get(damage.count());
                areaMap.get(areaGu).put(date, count);
            }
        });

        // 최종 결과 리스트 생성
        List<AreaDamageCountForDateListResponseDTO> resultList = areaMap.entrySet().stream()
                .map(entry -> new AreaDamageCountForDateListResponseDTO(entry.getKey(), entry.getValue().entrySet().stream()
                        .map(e -> AreaDamageCountForDateResponseDTO.builder().date(e.getKey()).count(e.getValue()).build())
                        .collect(Collectors.toList())))
                .collect(Collectors.toList());

        return new AreaForDateListResponseDTO(resultList);
    }

    @Override
    public AreaForMonthListResponseDTO getAreaDamageCountForMonth(AreaDamageCountForMonthServiceRequestDTO requestDTO) {
        YearMonth startMonth = requestDTO.getStart();
        YearMonth endMonth = requestDTO.getEndOrStart(); // 종료 월이 없으면 시작 월을 사용

        QDamageEntity damage = QDamageEntity.damageEntity;
        QAreaEntity area = QAreaEntity.areaEntity;

        // 모든 '구' 목록을 가져오기
        List<String> allAreas = queryFactory.select(area.areaGu)
                .from(area)
                .fetch();

        Map<String, Map<YearMonth, Long>> areaMap = new LinkedHashMap<>();

        // 모든 '구'에 대해 월별로 초기화
        for (String areaGu : allAreas) {
            Map<YearMonth, Long> monthCounts = new HashMap<>();
            for (YearMonth month = startMonth; !month.isAfter(endMonth); month = month.plusMonths(1)) {
                monthCounts.put(month, 0L);
            }
            areaMap.put(areaGu, monthCounts);
        }

        // 실제 데이터로 업데이트
        List<Tuple> rawResults = queryFactory
                .select(area.areaGu,
                        damage.createdDateTime.year(),
                        damage.createdDateTime.month(),
                        damage.count())
                .from(damage)
                .join(damage.areaEntity, area)
                .where(damage.createdDateTime.year().goe(startMonth.getYear())
                        .and(damage.createdDateTime.month().goe(startMonth.getMonthValue()).or(damage.createdDateTime.year().gt(startMonth.getYear())))
                        .and(damage.createdDateTime.year().loe(endMonth.getYear())
                                .and(damage.createdDateTime.month().loe(endMonth.getMonthValue()).or(damage.createdDateTime.year().lt(endMonth.getYear())))))
                .groupBy(area.areaGu, damage.createdDateTime.year(), damage.createdDateTime.month())
                .fetch();


        rawResults.forEach(tuple -> {
            String areaGu = tuple.get(area.areaGu);
            Optional<Integer> year = Optional.ofNullable(tuple.get(damage.createdDateTime.year()));
            Optional<Integer> month = Optional.ofNullable(tuple.get(damage.createdDateTime.month()));

            // 모든 날짜 컴포넌트가 있는 경우에만 날짜를 생성하고 맵 업데이트
            if (year.isPresent() && month.isPresent()) {
                YearMonth yearMonth = YearMonth.of(year.get(), month.get());
                Long count = tuple.get(damage.count());
                areaMap.get(areaGu).put(yearMonth, count);
            }
        });

        // 최종 결과 리스트 생성
        List<AreaDamageCountForMonthListResponseDTO> resultList = areaMap.entrySet().stream()
                .map(entry -> new AreaDamageCountForMonthListResponseDTO(entry.getKey(), entry.getValue().entrySet().stream()
                        .map(e -> new AreaDamageCountForMonthResponseDTO(e.getKey(), e.getValue()))
                        .collect(Collectors.toList())))
                .collect(Collectors.toList());

        return new AreaForMonthListResponseDTO(resultList);
    }

    @Override
    public boolean findDamageByHexagonIndexAndDtype(String hexagonIndex, String dtype) {
        QDamageEntity damage = QDamageEntity.damageEntity;

        return queryFactory
                .selectFrom(damage)
                .where(damage.hexagonEntity.hexagonIndex.eq(hexagonIndex)
                        .and(damage.dtype.eq(dtype))
                        .and(damage.status.in(Status.작업전, Status.작업중)))
                .setLockMode(LockModeType.PESSIMISTIC_WRITE) // Pessimistic Lock 설정
                .fetchFirst() != null;
    }

    @Override
    public List<TaskEntity> findTasksByProjectIdAndDamageStatus(Long projectId, Status status) {
        QTaskEntity taskEntity = QTaskEntity.taskEntity;
        QDamageEntity damageEntity = QDamageEntity.damageEntity;

        return queryFactory.selectFrom(taskEntity)
                .join(taskEntity.damageEntity, damageEntity)
                .where(taskEntity.projectEntity.id.eq(projectId)
                        .and(damageEntity.status.eq(status)))
                .fetch();
    }



@Override
public Page<DamageResponseDTO> findDamagesWithLatestTransaction(DamageSearchRequestDTO searchDTO, Pageable pageable) {
    QDamageEntity damage = QDamageEntity.damageEntity;
    QImageEntity image = QImageEntity.imageEntity;

    BooleanBuilder builder = new BooleanBuilder();

    builder.and(betweenDates(damage, searchDTO.getStart(), searchDTO.getEnd()))
            .and(equalToType(damage, searchDTO.getType()))
            .and(equalToStatus(damage, searchDTO.getStatus()))
            .and(equalToSeverity(damage, searchDTO.getSeverity()))
            .and(containsArea(damage, searchDTO.getArea()))
            .and(containsSearchWord(damage, searchDTO.getSearchWord()));

    List<DamageResponseDTO> results;

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
                    damage.createdDateTime,
                    damage.memberEntity.Id
            ))
            .from(damage)
            .where(builder)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(damage.createdDateTime.desc())
            .fetch();

    Long countResult = queryFactory
            .select(damage.count())
            .from(damage)
            .where(builder)
            .fetchOne();

    long total = (countResult != null) ? countResult : 0;

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

    return new PageImpl<>(results, pageable, total);

}

@Override
public StatisticListResponseDTO getStatistic(Long areaId) {
    QDamageEntity damage = QDamageEntity.damageEntity;
    QLocationEntity location = QLocationEntity.locationEntity;

    // 각 상태 및 심각도 3에 대한 조건부 카운트를 집계하는 쿼리
    List<Tuple> rawResults = queryFactory
            .select(location.id, location.locationName,
                    new CaseBuilder()
                            .when(damage.status.eq(Status.작업전))
                            .then(1).otherwise(0).sum().as("countDamageBefore"),
                    new CaseBuilder()
                            .when(damage.status.eq(Status.작업중))
                            .then(1).otherwise(0).sum().as("countDamageDuring"),
                    new CaseBuilder()
                            .when(damage.status.eq(Status.작업완료))
                            .then(1).otherwise(0).sum().as("countDamageDone"),
                    new CaseBuilder()
                            .when(damage.severity.eq(3))
                            .then(1).otherwise(0).sum().as("severityCount") // 심각도 3 조건 추가
            )
            .from(location)
            .leftJoin(location.damageEntities, damage)
            .where(location.areaEntity.id.eq(areaId))
            .groupBy(location.id)
            .fetch();

    // 결과 처리
    List<StatisticLocationSeverityCountResponseDTO> results = rawResults.stream()
            .map(tuple -> new StatisticLocationSeverityCountResponseDTO(
                    tuple.get(location.locationName),
                    Optional.ofNullable(tuple.get(Expressions.numberPath(Integer.class, "countDamageBefore"))).orElse(0).longValue(),
                    Optional.ofNullable(tuple.get(Expressions.numberPath(Integer.class, "countDamageDuring"))).orElse(0).longValue(),
                    Optional.ofNullable(tuple.get(Expressions.numberPath(Integer.class, "countDamageDone"))).orElse(0).longValue(),
                    Optional.ofNullable(tuple.get(Expressions.numberPath(Integer.class, "severityCount"))).orElse(0).longValue()
            ))
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
                    entry.getValue().getOrDefault(Status.작업중, 0L),
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
                    entry.getValue().getOrDefault(Status.작업중, 0L),
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

private BooleanExpression equalToType(QDamageEntity damage, String type) {
    if (type != null) {
        return damage.dtype.eq(type);
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
