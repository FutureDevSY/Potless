package com.potless.backend.project.repository.project;

import com.potless.backend.project.entity.ProjectEntity;
import com.potless.backend.project.repository.project.custom.ProjectRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Long>, ProjectRepositoryCustom {

}
