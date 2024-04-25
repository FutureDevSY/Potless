package Potless.Backend.project.repository.task.custom;

import Potless.Backend.project.entity.TaskEntity;

import java.util.List;

public interface TaskRepositoryCustom {
    List<TaskEntity> findTasksByProjectId(Long projectId);
}
