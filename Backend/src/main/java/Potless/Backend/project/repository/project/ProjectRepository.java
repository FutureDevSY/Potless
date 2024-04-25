package Potless.Backend.project.repository.project;

import Potless.Backend.project.entity.ProjectEntity;
import Potless.Backend.project.repository.project.custom.ProjectRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Long>, ProjectRepositoryCustom {

}
