package Potless.Backend.member.repository.member.custom;

import Potless.Backend.member.entity.MemberEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static Potless.Backend.member.entity.QMemberEntity.memberEntity;

@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom{
    private final JPAQueryFactory query;

    @Override
    public Optional<MemberEntity> searchByEmail(String email) {
        return Optional.ofNullable(query.selectFrom(memberEntity)
                .where(memberEntity.email.eq(email))
                .fetchFirst());
    }

}
