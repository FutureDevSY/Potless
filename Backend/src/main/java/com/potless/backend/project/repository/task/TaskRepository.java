package com.potless.backend.project.repository.task;

import com.potless.backend.project.entity.TaskEntity;
import com.potless.backend.project.repository.task.custom.TaskRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<TaskEntity, Long>, TaskRepositoryCustom {

}
