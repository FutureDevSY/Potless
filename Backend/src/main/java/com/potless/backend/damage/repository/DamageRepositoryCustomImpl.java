package com.potless.backend.damage.repository;

import com.potless.backend.damage.dto.controller.request.DamageSearchRequestDTO;
import com.potless.backend.damage.dto.controller.response.DamageResponseDTO;
import com.potless.backend.damage.dto.controller.response.ImagesResponseDTO;
import com.potless.backend.damage.entity.enums.Status;
import com.potless.backend.damage.entity.road.QCrackEntity;
import com.potless.backend.damage.entity.road.QDamageEntity;
import com.potless.backend.damage.entity.road.QImageEntity;
import com.potless.backend.damage.entity.road.QPotholeEntity;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class DamageRepositoryCustomImpl implements DamageRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public DamageRepositoryCustomImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

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

        if ("CRACK".equals(searchDTO.getType())) {
            QCrackEntity crack = QCrackEntity.crackEntity;

            List<DamageResponseDTO> results = queryFactory
                    .select(Projections.constructor(DamageResponseDTO.class,
                            damage.id,
                            damage.severity,
                            damage.dirX,
                            damage.dirY,
                            damage.address,
                            damage.roadName,
                            damage.width,
                            damage.status,
                            damage.areaEntity.areaGu,
                            damage.locationEntity.locationName,
                            JPAExpressions
                                    .select(Projections.constructor(ImagesResponseDTO.class,
                                            image.id,
                                            image.url,
                                            image.order))
                                    .from(image)
                                    .where(image.damageEntity.id.eq(damage.id)),
                            damage.dtype
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

            List<DamageResponseDTO> results = queryFactory
                    .select(Projections.constructor(DamageResponseDTO.class,
                            damage.id,
                            damage.severity,
                            damage.dirX,
                            damage.dirY,
                            damage.address,
                            damage.roadName,
                            damage.width,
                            damage.status,
                            damage.areaEntity.areaGu,
                            damage.locationEntity.locationName,
                            JPAExpressions
                                    .select(Projections.constructor(ImagesResponseDTO.class,
                                            image.id,
                                            image.url,
                                            image.order))
                                    .from(image)
                                    .where(image.damageEntity.id.eq(damage.id)),
                            damage.dtype
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
            List<DamageResponseDTO> results = queryFactory
                    .select(Projections.constructor(DamageResponseDTO.class,
                            damage.id,
                            damage.severity,
                            damage.dirX,
                            damage.dirY,
                            damage.address,
                            damage.roadName,
                            damage.width,
                            damage.status,
                            damage.areaEntity.areaGu,
                            damage.locationEntity.locationName,
                            JPAExpressions
                                    .select(Projections.constructor(ImagesResponseDTO.class,
                                            image.id,
                                            image.url,
                                            image.order))
                                    .from(image)
                                    .where(image.damageEntity.id.eq(damage.id)),
                            damage.dtype
                    ))
                    .from(damage)
                    .where(builder)
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();

            Long countResult = queryFactory
                    .select(damage.count())
                    .from(damage)
                    .where(builder)
                    .fetchOne();

            long total = (countResult != null) ? countResult : 0;

            return new PageImpl<>(results, pageable, total);
        }
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
            BooleanExpression roadNameMatch = damage.roadName.contains(searchWord);
            BooleanExpression locationNameMatch = damage.locationEntity.locationName.contains(searchWord);
            return roadNameMatch.or(locationNameMatch);
        }
        return null;
    }
}
