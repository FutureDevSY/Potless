package com.potless.backend.member.repository.member.custom;


import com.potless.backend.member.entity.MemberEntity;

import java.util.List;
import java.util.Optional;

public interface MemberRepositoryCustom {
    Optional<MemberEntity> searchByEmail(String email);

    List<MemberEntity> findAllByAreaId(Long areaId);
}
