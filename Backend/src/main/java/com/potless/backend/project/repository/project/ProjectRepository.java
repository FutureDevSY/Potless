package com.potless.backend.project.repository.project;

import com.potless.backend.project.entity.ProjectEntity;
import com.potless.backend.project.repository.project.custom.ProjectRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Long>, ProjectRepositoryCustom {
//    @Query("SELECT p FROM ProjectEntity p JOIN FETCH p.managerEntity m JOIN FETCH m.memberEntity WHERE p.id = :projectId")
//    Optional<ProjectEntity> findWithDetailsById(@Param("projectId") Long projectId);

}
