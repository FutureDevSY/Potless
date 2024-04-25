package com.potless.backend.member.repository.manager;

import com.potless.backend.member.entity.ManagerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerRepository extends JpaRepository<ManagerEntity, Long> {
}
