package com.potless.backend.member.repository.member.custom;

import com.potless.backend.member.entity.MemberEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

import static com.potless.backend.member.entity.QMemberEntity.memberEntity;


@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {
    private final JPAQueryFactory query;

    @Override
    public Optional<MemberEntity> searchByEmail(String email) {
        return Optional.ofNullable(query.selectFrom(memberEntity)
                .where(memberEntity.email.eq(email))
                .fetchFirst());
    }

    @Override
    public List<MemberEntity> findAllByAreaId(Long areaId) {
        return query.selectFrom(memberEntity)
                .where(memberEntity.area.id.eq(areaId).and(memberEntity.role.eq(1)))
                .fetch();
    }

}
