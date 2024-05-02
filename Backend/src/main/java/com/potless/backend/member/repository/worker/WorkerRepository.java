package com.potless.backend.member.repository.worker;

import com.potless.backend.member.entity.TeamEntity;
import com.potless.backend.member.entity.WorkerEntity;
import com.potless.backend.member.repository.worker.custom.WorkerRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WorkerRepository extends JpaRepository<WorkerEntity, Long>, WorkerRepositoryCustom {
}
