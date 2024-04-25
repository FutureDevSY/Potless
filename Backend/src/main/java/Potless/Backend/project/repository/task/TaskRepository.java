package Potless.Backend.project.repository.task;

import Potless.Backend.project.entity.TaskEntity;
import Potless.Backend.project.repository.task.custom.TaskRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<TaskEntity, Long>, TaskRepositoryCustom {

}
