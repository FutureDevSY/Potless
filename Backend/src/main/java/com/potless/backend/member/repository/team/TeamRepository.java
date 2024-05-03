package com.potless.backend.member.repository.team;

import com.potless.backend.member.entity.TeamEntity;
import com.potless.backend.member.repository.team.custom.TeamRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<TeamEntity, Long>, TeamRepositoryCustom {
}
