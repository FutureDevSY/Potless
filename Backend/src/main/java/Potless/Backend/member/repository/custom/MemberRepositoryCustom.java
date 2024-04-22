package Potless.Backend.member.repository.custom;

import Potless.Backend.member.entity.MemberEntity;

import java.util.Optional;

public interface MemberRepositoryCustom{
    Optional<MemberEntity> searchByEmail(String email);
}
