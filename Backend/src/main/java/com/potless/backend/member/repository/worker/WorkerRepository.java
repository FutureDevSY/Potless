package com.potless.backend.member.repository.worker;

import com.potless.backend.member.entity.TeamEntity;
import com.potless.backend.member.entity.WorkerEntity;
import com.potless.backend.member.repository.worker.custom.WorkerRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkerRepository extends JpaRepository<WorkerEntity, Long>, WorkerRepositoryCustom {

}
